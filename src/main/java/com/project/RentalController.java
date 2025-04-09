package com.project;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

// Rename class to RentalController
public class RentalController {

    private static final int STATUS_LOADING = 0;
    private static final int STATUS_ADD = 1;
    private static final int STATUS_MODIFY = 2;
    private int status = STATUS_ADD;

    private ProducteView view;
    private ArrayList<CategoriaModel> listCategories;
    private ArrayList<ProducteModel> listProducts;
    private ArrayList<ClientsModel> listClients;
    private JTabbedPane tabbedPane;

    public RentalController(ProducteView view, JTabbedPane tabbedPane) {
        this.view = view;
        this.listProducts = new ArrayList<>();
        this.tabbedPane = tabbedPane;
    }

    // Acciones para iniciar el controlador
    public void start() {
        setupActionListeners();
        setStatus(STATUS_ADD); // Forzar estado inicial a ADD
        loadData();
        // No pongas más instrucciones aquí,
        // porque hemos abierto un hilo en paralelo en 'loadData'
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
        // Gestionar datos del DAO en una tarea paralela
        UtilsSwingThread.run(() -> {
            int oldStatus = status;

            // Desactiva todos los elementos y muestra el 'Cargando ...'
            setStatus(STATUS_LOADING);

            // Actualizamos las listas
            listCategories = CategoriaDAO.getAll();
            listProducts = ProducteDAO.getAll();
            listClients = ClientsDAO.getAll();

            // Simula espera
            Thread.sleep(1500);

            // Define el modelo elegido
            if (!listProducts.isEmpty()) {
                int oldSelected = view.itemComboBox.getSelectedIndex();
                int newSelected = 0;

                if (oldSelected != -1) {
                    String selectedName = view.itemComboBox.getItemAt(oldSelected);
                    ProducteModel tmp = getListModelFromName(selectedName);
                    if (tmp != null) {
                        newSelected = listProducts.indexOf(tmp);
                    }
                }

                view.itemComboBox.removeAllItems();

                for (ProducteModel itemModel : listProducts) {
                    // Get vehicle and client info for better display
                    CategoriaModel vehicle = CategoriaDAO.getItem(itemModel.getIdVehicle());
                    ClientsModel client = ClientsDAO.getItem(itemModel.getIdClient());
                    
                    String vehicleInfo = vehicle != null ? vehicle.getMarca() + " " + vehicle.getModel() : "Unknown";
                    String clientInfo = client != null ? client.getNom() : "Unknown";
                    
                    // Format the display string with vehicle, client and dates
                    String displayText = String.format("%s | %s | %s - %s",
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
        // No poner más instrucciones después del UtilsSwingThread.run
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

    // Fix checkbox state management
    private void controllerNewItemCheckBoxAction(ItemEvent evt) {
        boolean isNew = evt.getStateChange() == ItemEvent.SELECTED;
        setStatus(isNew ? STATUS_ADD : STATUS_MODIFY);
        
        // Force proper state
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
            ProducteModel newModel = new ProducteModel(data_inici, data_final, id_vehicle, id_client);

            setStatus(STATUS_MODIFY);
            ProducteDAO.addItem(newModel);
            loadData();
        });
    }

    private void controllerModifyButtonAction(ActionEvent e) {
        UtilsSwingThread.run(() -> {
            int index = view.itemComboBox.getSelectedIndex();
            if (index != -1) {
                ProducteModel modifiedModel = getModelFromComboBoxIndex(index);
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

                    ProducteDAO.updateItem(modifiedModel);
                    loadData();
                }
            }
        });
    }

    private void controllerDeleteButtonAction(ActionEvent e) {
        UtilsSwingThread.run(() -> {
            int index = view.itemComboBox.getSelectedIndex();
            if (index != -1) {
                ProducteModel modifiedModel = getModelFromComboBoxIndex(index);
                if (modifiedModel != null) {
                    ProducteDAO.deleteItem(modifiedModel.getId());
                    loadData();
                }
            }
        });
    }

    private ProducteModel getModelFromComboBoxIndex(int index) {
        if (index != -1) {
            String comboBoxText = view.itemComboBox.getItemAt(index);
            return getListModelFromName(comboBoxText);
        }
        return null;
    }

    private ProducteModel getListModelFromName(String searchName) {
        for (ProducteModel model : listProducts) {
            if (searchName.contains(model.getDataInici())) {
                return model;
            }
        }
        return null;
    }

    private void fillFormData() {
        view.categoryComboBox.removeAllItems();
        for (CategoriaModel category : listCategories) {
            view.categoryComboBox.addItem(category.getMarca() + " " + category.getModel());
        }

        view.clientComboBox.removeAllItems();
        for (ClientsModel client : listClients) {
            view.clientComboBox.addItem(client.getNom());
        }

        int selectedEntry = view.itemComboBox.getSelectedIndex();
        if (selectedEntry != -1 && status == STATUS_MODIFY) {
            ProducteModel selectedItem = listProducts.get(selectedEntry);
            view.itemNameField.setText(selectedItem.getDataInici());
            view.itemDescriptionField.setText(selectedItem.getDataFinal());

            int categoryIndex = getCategoryIndexById(selectedItem.getIdVehicle());
            int clientIndex = getClientIndexById(selectedItem.getIdClient());

            view.categoryComboBox.setSelectedIndex(categoryIndex);
            view.clientComboBox.setSelectedIndex(clientIndex);
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
