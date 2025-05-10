package com.project;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.*;

public class CategoriaController {

    final private int STATUS_LOADING = 0;
    final private int STATUS_ADD = 1;
    final private int STATUS_MODIFY = 2;
    private int status = STATUS_ADD;

    private CategoriaView view;
    private ArrayList<CategoriaModel> list;
    private JTabbedPane tabbedPane;
    private String selectedImagePath = null;
    private static final String IMAGE_DIRECTORY = "vehicle_images/";

    CategoriaController(CategoriaView view, JTabbedPane tabbedPane) {
        this.view = view;
        this.list = new ArrayList<>();
        this.tabbedPane = tabbedPane;
    }
    public void start() {
        setupActionListeners();
        loadData();
    }

    private void setupActionListeners() {
        view.reloadButton.addActionListener(this::controllerReloadButtonAction);
        view.newItemCheckBox.addItemListener(this::controllerNewItemCheckBoxAction);
        view.itemComboBox.addItemListener(this::controllerItemComboBoxAction);
        view.addButton.addActionListener(this::controllerAddButtonAction);
        view.modifyButton.addActionListener(this::controllerModifyButtonAction);
        view.deleteButton.addActionListener(this::controllerDeleteButtonAction);
        view.selectImageButton.addActionListener(this::controllerSelectImageButtonAction);
    }

    public void loadData() {

        int oldStatus = status;
        setStatus(STATUS_LOADING);
        UtilsSwingThread.run(
            () -> {
                list = CategoriaDAO.getAll();
                Thread.sleep(1500);
                if (!list.isEmpty()) {
                    int oldSelected = view.itemComboBox.getSelectedIndex();
                    int newSelected = 0;
                    if (oldSelected != -1) {
                        String selectedName = view.itemComboBox.getItemAt(oldSelected);
                        CategoriaModel tmp = getListModelFromName(selectedName);
                        if (tmp != null) {
                            newSelected = list.indexOf(tmp);
                        }
                    }
                    view.itemComboBox.removeAllItems();
                    for (CategoriaModel itemModel : list) {
                        view.itemComboBox.addItem(itemModel.getMarca() + " " + itemModel.getModel());
                    }
                    view.itemComboBox.setSelectedIndex(newSelected);
                }
                setStatus(oldStatus);
                fillFormData();
            }
        );
    }
    private void setStatus(int newStatus) {
        Boolean isLoading = false;

        status = newStatus;
        if (status == STATUS_LOADING) {
            isLoading = true;
        } else if (status == STATUS_ADD) {
            view.newItemCheckBox.setSelected(true);
        } else if (status == STATUS_MODIFY) {
            view.newItemCheckBox.setSelected(false);
        }

        tabbedPane.setEnabled(!isLoading);
        view.reloadButton.setEnabled(!isLoading);
        view.loadingLabel.setVisible(isLoading);
        view.newItemCheckBox.setEnabled(!isLoading);
        view.itemComboBox.setEnabled(!isLoading && status == STATUS_MODIFY);
        view.itemNameField.setEnabled(!isLoading);
        view.itemName2Field.setEnabled(!isLoading);
        view.itemYearField.setEnabled(!isLoading);
        view.itemAvailabilityCheckBox.setEnabled(!isLoading); // Cambiar el campo de texto por checkbox
        view.itemPhotoField.setEnabled(!isLoading);
        view.addButton.setEnabled(!isLoading && status == STATUS_ADD);
        view.modifyButton.setEnabled(!isLoading && status == STATUS_MODIFY);
        view.deleteButton.setEnabled(!isLoading && status == STATUS_MODIFY);
    }
    private void controllerReloadButtonAction(ActionEvent e) {
        loadData();
    }
    private void controllerNewItemCheckBoxAction(ItemEvent evt) {
        boolean isNew = evt.getStateChange() == ItemEvent.SELECTED;
        setStatus(isNew ? STATUS_ADD : STATUS_MODIFY);
        view.newItemCheckBox.setSelected(isNew);
        view.itemComboBox.setEnabled(!isNew);
        
        fillFormData();
    }
    private void controllerItemComboBoxAction (ItemEvent e) {
        fillFormData();
    }
    private void controllerAddButtonAction(ActionEvent e) {
        String marca = view.itemNameField.getText();
        String model = view.itemName2Field.getText();
        String any = view.itemYearField.getText();
        String disponibilitat = view.itemAvailabilityCheckBox.isSelected() ? "Disponible" : "No disponible";
        String foto = view.itemPhotoField.getText();

        CategoriaModel newModel = new CategoriaModel(marca, model, any, disponibilitat, foto);

        setStatus(STATUS_MODIFY);
        UtilsSwingThread.run(
            () -> {
                CategoriaDAO.addItem(newModel);
                int index = view.itemComboBox.getItemCount() - 1;
                view.itemComboBox.insertItemAt(marca + " " + model, index);
                view.itemComboBox.setSelectedIndex(index);
                view.itemNameField.setText(marca);
                view.itemName2Field.setText(model);
                view.itemYearField.setText(any);
                view.itemAvailabilityCheckBox.setSelected("Disponible".equals(disponibilitat));
                view.itemPhotoField.setText(foto);
                loadData();
            }
        );
    }
    private void controllerModifyButtonAction(ActionEvent e) {
        String marca = view.itemNameField.getText();
        String model = view.itemName2Field.getText();
        String any = view.itemYearField.getText();
        String disponibilitat = view.itemAvailabilityCheckBox.isSelected() ? "Disponible" : "No disponible";
        String foto = view.itemPhotoField.getText();
        int index = view.itemComboBox.getSelectedIndex();
        CategoriaModel modifiedModel = getModelFromComboBoxIndex(index);
        modifiedModel.setMarca(marca);
        modifiedModel.setModel(model);
        modifiedModel.setAny(any);
        modifiedModel.setDisponibilitat(disponibilitat);
        modifiedModel.setFoto(foto);
        UtilsSwingThread.run(
            () -> {
                CategoriaDAO.updateItem(modifiedModel);
                view.itemComboBox.removeItemAt(index);
                view.itemComboBox.insertItemAt(marca + " " + model, index);
                view.itemComboBox.setSelectedIndex(index);
                loadData();
            }
        );
    }
    private void controllerDeleteButtonAction(ActionEvent e) {
        int index = view.itemComboBox.getSelectedIndex();
        CategoriaModel modifiedModel = getModelFromComboBoxIndex(index);
        UtilsSwingThread.run(
            () -> {
                CategoriaDAO.deleteItem(modifiedModel.getId());
                loadData();
            }
        );
    }

    private void controllerSelectImageButtonAction(ActionEvent e) {
        int result = view.fileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = view.fileChooser.getSelectedFile();
            try {
                File projectDir = new File(System.getProperty("user.dir"));
                File imageDir = new File(projectDir, IMAGE_DIRECTORY);
                if (!imageDir.exists()) {
                    imageDir.mkdirs();
                }
                String uniqueFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                String relativePath = IMAGE_DIRECTORY + uniqueFileName;
                Path destination = new File(projectDir, relativePath).toPath();
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                selectedImagePath = relativePath;
                view.itemPhotoField.setText(relativePath);
                displayImage(relativePath);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, 
                    "Error al carregar la imatge: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File(imagePath);
                if (!imageFile.isAbsolute()) {
                    imageFile = new File(System.getProperty("user.dir"), imagePath);
                }

                if (imageFile.exists()) {
                    ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
                    Image image = imageIcon.getImage();
                    Image scaledImage = image.getScaledInstance(180, 140, Image.SCALE_SMOOTH);
                    view.imageLabel.setIcon(new ImageIcon(scaledImage));
                    view.imageLabel.setText("");
                } else {
                    view.imageLabel.setIcon(null);
                    view.imageLabel.setText("Image not found: " + imagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                view.imageLabel.setIcon(null);
                view.imageLabel.setText("Error loading image");
            }
        } else {
            view.imageLabel.setIcon(null);
            view.imageLabel.setText("No image");
        }
    }

    private CategoriaModel getModelFromComboBoxIndex(int index) {
        String comboBoxText = view.itemComboBox.getItemAt(index);
        return getListModelFromName(comboBoxText);
    }

    private CategoriaModel getListModelFromName(String searchName) {
        CategoriaModel rst = null;
        for (int i=0; i<list.size(); i++) {
            String listName = list.get(i).getMarca() + " " + list.get(i).getModel() ;
            if (searchName.compareTo(listName) == 0) {
                rst = list.get(i);
                break;
            }
        }
        return rst;
    }
    private void fillFormData() {
        int selectedEntry = view.itemComboBox.getSelectedIndex();
        if (selectedEntry != -1 && status == STATUS_MODIFY) {
            view.itemNameField.setText(list.get(selectedEntry).getMarca());
            view.itemName2Field.setText(list.get(selectedEntry).getModel());
            view.itemYearField.setText(list.get(selectedEntry).getAny());
            view.itemAvailabilityCheckBox.setSelected("Disponible".equals(list.get(selectedEntry).getDisponibilitat()));
            view.itemPhotoField.setText(list.get(selectedEntry).getFoto());
            String photoPath = list.get(selectedEntry).getFoto();
            view.itemPhotoField.setText(photoPath);
            displayImage(photoPath);
        } else {
            view.itemNameField.setText("");
            view.itemName2Field.setText("");
            view.itemYearField.setText("");
            view.itemAvailabilityCheckBox.setSelected(true);
            view.itemPhotoField.setText("");
            view.imageLabel.setIcon(null);
            view.imageLabel.setText("No image");
        }
    }
}