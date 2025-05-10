package com.project;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class RentalController {

    private static final int STATUS_LOADING = 0;
    private static final int STATUS_ADD = 1;
    private static final int STATUS_MODIFY = 2;
    private int status = STATUS_ADD;

    private RentalView view;
    private ArrayList<CategoriaModel> listCategories; // Ahora esta lista solo contendrá vehículos disponibles
    private ArrayList<RentalModel> listProducts;
    private ArrayList<ClientsModel> listClients;
    private JTabbedPane tabbedPane;

    public RentalController(RentalView view, JTabbedPane tabbedPane) {
        this.view = view;
        this.listProducts = new ArrayList<>();
        this.tabbedPane = tabbedPane;
    }
    public void start() {
        setupActionListeners();
        setStatus(STATUS_ADD); // Forzar estado inicial a ADD
        loadData();
    }

    private void setupActionListeners() {
        view.reloadButton.addActionListener(this::controllerReloadButtonAction);
        view.newItemCheckBox.addItemListener(this::controllerNewItemCheckBoxAction);
        view.itemComboBox.addItemListener(this::controllerItemComboBoxAction);
        view.addButton.addActionListener(this::controllerAddButtonAction);
        view.modifyButton.addActionListener(this::controllerModifyButtonAction);
        view.deleteButton.addActionListener(this::controllerDeleteButtonAction);
    }

    public void loadData() {
        UtilsSwingThread.run(() -> {
            int oldStatus = status;
            setStatus(STATUS_LOADING);
            listCategories = CategoriaDAO.getAvailableVehicles(); // Solo vehículos disponibles
            listProducts = RentalDAO.getAll();
            listClients = ClientsDAO.getAll();
            Thread.sleep(1500);
            if (!listProducts.isEmpty()) {
                int oldSelected = view.itemComboBox.getSelectedIndex();
                int newSelected = 0;

                if (oldSelected != -1) {
                    String selectedName = view.itemComboBox.getItemAt(oldSelected);
                    RentalModel tmp = getListModelFromName(selectedName);
                    if (tmp != null) {
                        newSelected = listProducts.indexOf(tmp);
                    }
                }

                view.itemComboBox.removeAllItems();

                for (RentalModel itemModel : listProducts) {
                    CategoriaModel vehicle = CategoriaDAO.getItem(itemModel.getIdVehicle());
                    ClientsModel client = ClientsDAO.getItem(itemModel.getIdClient());
                    
                    String vehicleInfo = vehicle != null ? vehicle.getMarca() + " " + vehicle.getModel() : "Unknown";
                    String clientInfo = client != null ? client.getNom() : "Unknown";
                    String displayText = String.format("%s | %s | %s / %s",
                        vehicleInfo,
                        clientInfo,
                        itemModel.getDataInici(),
                        itemModel.getDataFinal());
                        
                    view.itemComboBox.addItem(displayText);
                }

                view.itemComboBox.setSelectedIndex(newSelected);
            }

            setStatus(oldStatus);
            fillFormData();
     
        });
    }

    private void setStatus(int newStatus) {
        boolean isLoading = (newStatus == STATUS_LOADING);
        status = newStatus;

        tabbedPane.setEnabled(!isLoading);
        view.reloadButton.setEnabled(!isLoading);
        view.loadingLabel.setVisible(isLoading);
        view.newItemCheckBox.setEnabled(!isLoading);
        view.itemComboBox.setEnabled(!isLoading && status == STATUS_MODIFY);
        view.itemNameField.setEnabled(!isLoading);
        view.itemDescriptionField.setEnabled(!isLoading);
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

    private void controllerItemComboBoxAction(ItemEvent e) {
        fillFormData();
    }

    private void controllerAddButtonAction(ActionEvent e) {
        UtilsSwingThread.run(() -> {
            String data_inici = view.itemNameField.getText();
            String data_final = view.itemDescriptionField.getText();
            String selectedCategoryName = (String) view.categoryComboBox.getSelectedItem();
            String selectedClientName = (String) view.clientComboBox.getSelectedItem();
            int id_vehicle = getCategoryIDFromName(selectedCategoryName);
            int id_client = getClientIDFromName(selectedClientName);
            if (DisponibilitatDAO.isVehicleAvailable(id_vehicle)) {
                RentalModel newModel = new RentalModel(data_inici, data_final, id_vehicle, id_client);
                RentalDAO.addItem(newModel);
                DisponibilitatDAO.updateVehicleAvailability(id_vehicle, "No disponible");
                
                setStatus(STATUS_MODIFY);
                loadData();
            } else {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(view, 
                        "El vehículo seleccionado no está disponible para alquiler.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }

    private void controllerModifyButtonAction(ActionEvent e) {
        UtilsSwingThread.run(() -> {
            int index = view.itemComboBox.getSelectedIndex();
            if (index != -1) {
                RentalModel modifiedModel = getModelFromComboBoxIndex(index);
                if (modifiedModel != null) {
                    String data_inici = view.itemNameField.getText();
                    String data_final = view.itemDescriptionField.getText();
                    String selectedCategoryName = (String) view.categoryComboBox.getSelectedItem();
                    String selectedClientName = (String) view.clientComboBox.getSelectedItem();
                    int id_vehicle = getCategoryIDFromName(selectedCategoryName);
                    int id_client = getClientIDFromName(selectedClientName);

                    modifiedModel.setDataInici(data_inici);
                    modifiedModel.setDataFinal(data_final);
                    modifiedModel.setIdVehicle(id_vehicle);
                    modifiedModel.setIdClient(id_client);

                    RentalDAO.updateItem(modifiedModel);
                    loadData();
                }
            }
        });
    }

    private void controllerDeleteButtonAction(ActionEvent e) {
        UtilsSwingThread.run(() -> {
            int index = view.itemComboBox.getSelectedIndex();
            if (index != -1) {
                RentalModel modifiedModel = getModelFromComboBoxIndex(index);
                if (modifiedModel != null) {
                    RentalDAO.deleteItem(modifiedModel.getId());
                    loadData();
                }
            }
        });
    }

    private RentalModel getModelFromComboBoxIndex(int index) {
        if (index != -1) {
            String comboBoxText = view.itemComboBox.getItemAt(index);
            return getListModelFromName(comboBoxText);
        }
        return null;
    }

    private RentalModel getListModelFromName(String searchName) {
        for (RentalModel model : listProducts) {
            if (searchName.contains(model.getDataInici())) {
                return model;
            }
        }
        return null;
    }

    private void fillFormData() {
        view.categoryComboBox.removeAllItems();
        for (CategoriaModel category : listCategories) {
            if ("Disponible".equals(category.getDisponibilitat())) {
                view.categoryComboBox.addItem(category.getMarca() + " " + category.getModel());
            }
        }

        view.clientComboBox.removeAllItems();
        for (ClientsModel client : listClients) {
            view.clientComboBox.addItem(client.getNom());
        }

        int selectedEntry = view.itemComboBox.getSelectedIndex();
        if (selectedEntry != -1 && status == STATUS_MODIFY) {
            RentalModel selectedItem = listProducts.get(selectedEntry);
            view.itemNameField.setText(selectedItem.getDataInici());
            view.itemDescriptionField.setText(selectedItem.getDataFinal());
            CategoriaModel vehicle = CategoriaDAO.getItem(selectedItem.getIdVehicle());
            int clientIndex = getClientIndexById(selectedItem.getIdClient());
            for (int i = 0; i < view.categoryComboBox.getItemCount(); i++) {
                String categoryName = view.categoryComboBox.getItemAt(i);
                if (categoryName.equals(vehicle.getMarca() + " " + vehicle.getModel())) {
                    view.categoryComboBox.setSelectedIndex(i);
                    break;
                }
            }

            if (clientIndex >= 0) {
                view.clientComboBox.setSelectedIndex(clientIndex);
            }
        } else {
            view.itemNameField.setText("");
            view.itemDescriptionField.setText("");
        }
    }

    private int getCategoryIndexById(int id) {
        for (int i = 0; i < listCategories.size(); i++) {
            if (listCategories.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private int getClientIndexById(int id) {
        for (int i = 0; i < listClients.size(); i++) {
            if (listClients.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private int getCategoryIDFromName(String categoryName) {
        for (CategoriaModel category : listCategories) {
            if ((category.getMarca() + " " + category.getModel()).equals(categoryName)) {
                return category.getId();
            }
        }
        return 0; // Retorna un valor por defecto si no se encuentra la categoría
    }

    private int getClientIDFromName(String clientName) {
        for (ClientsModel client : listClients) {
            if (client.getNom().equals(clientName)) {
                return client.getId();
            }
        }
        return 0; // Retorna un valor por defecto si no se encuentra el cliente
    }
}
