package com.project;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

import com.project.CategoriaModel;
import com.project.UtilsSwingThread;

public class ClientsVehiclesController {

    private static final int STATUS_LOADING = 0;
    private static final int STATUS_ADD = 1;
    private static final int STATUS_MODIFY = 2;
    private int status = STATUS_ADD;

    private ClientsVehiclesView view;
    private ArrayList<CategoriaModel> listCategories;
    private ArrayList<ClientsVehiclesModel> listClientsVehicles;
    private ArrayList<ClientsModel> listClients;
    private JTabbedPane tabbedPane;

    public ClientsVehiclesController(ClientsVehiclesView view, JTabbedPane tabbedPane) {
        this.view = view;
        this.listClientsVehicles = new ArrayList<>();
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
            listCategories = CategoriaDAO.getAll();
            listClientsVehicles = ClientsVehiclesDAO.getAll();
            listClients = ClientsDAO.getAll();
            Thread.sleep(1500);
            if (!listClientsVehicles.isEmpty()) {
                int oldSelected = view.itemComboBox.getSelectedIndex();
                int newSelected = 0;

                if (oldSelected != -1) {
                    String selectedName = view.itemComboBox.getItemAt(oldSelected);
                    ClientsVehiclesModel tmp = getListModelFromName(selectedName);
                    if (tmp != null) {
                        newSelected = listClientsVehicles.indexOf(tmp);
                    }
                }

                view.itemComboBox.removeAllItems();

                for (ClientsVehiclesModel itemModel : listClientsVehicles) {
                    int clientId = itemModel.getIdClient();
                    int vehicleId = itemModel.getIdVehicle();
                
                    String clientName = getClientNameById(clientId);
                    String vehicleBrand = getVehicleBrandById(vehicleId);
                    String rentalDates = getRentalDatesForClientAndVehicle(clientId, vehicleId);
                
                    view.itemComboBox.addItem(String.format("%s | %s | %s", 
                        vehicleBrand, clientName, rentalDates));
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
        view.itemObservationsField.setEnabled(!isLoading);
        view.categoryComboBox.setEnabled(!isLoading && status == STATUS_ADD);
        view.clientComboBox.setEnabled(!isLoading && status == STATUS_ADD);
        
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
        view.categoryComboBox.setEnabled(isNew);
        view.clientComboBox.setEnabled(isNew);
        
        fillFormData();
    }

    private void controllerItemComboBoxAction(ItemEvent e) {
        fillFormData();
    }

    private void controllerAddButtonAction(ActionEvent e) {
        UtilsSwingThread.run(() -> {
            String observacions = view.itemObservationsField.getText();
            String selectedCategoryName = (String) view.categoryComboBox.getSelectedItem();
            String selectedClientName = (String) view.clientComboBox.getSelectedItem();
            int id_vehicle = getCategoryIDFromName(selectedCategoryName);
            int id_client = getClientIDFromName(selectedClientName);
            ClientsVehiclesModel newModel = new ClientsVehiclesModel(id_client, id_vehicle, observacions);

            setStatus(STATUS_MODIFY);
            ClientsVehiclesDAO.addItem(newModel);
            loadData();
        });
    }

    private void controllerModifyButtonAction(ActionEvent e) {
        UtilsSwingThread.run(() -> {
            int index = view.itemComboBox.getSelectedIndex();
            if (index != -1) {
                ClientsVehiclesModel modifiedModel = getModelFromComboBoxIndex(index);
                if (modifiedModel != null) {
                    String observacions = view.itemObservationsField.getText();
                    String selectedCategoryName = (String) view.categoryComboBox.getSelectedItem();
                    String selectedClientName = (String) view.clientComboBox.getSelectedItem();
                    int id_vehicle = getCategoryIDFromName(selectedCategoryName);
                    int id_client = getClientIDFromName(selectedClientName);

                    modifiedModel.setIdClient(id_client);
                    modifiedModel.setIdVehicle(id_vehicle);
                    modifiedModel.setObservacions(observacions);

                    ClientsVehiclesDAO.updateItem(modifiedModel);
                    loadData();
                }
            }
        });
    }

    private void controllerDeleteButtonAction(ActionEvent e) {
        UtilsSwingThread.run(() -> {
            int index = view.itemComboBox.getSelectedIndex();
            if (index != -1) {
                ClientsVehiclesModel modifiedModel = getModelFromComboBoxIndex(index);
                if (modifiedModel != null) {
                    ClientsVehiclesDAO.deleteItem(modifiedModel.getId());
                    loadData();
                }
            }
        });
    }

    private ClientsVehiclesModel getModelFromComboBoxIndex(int index) {
        if (index != -1) {
            return listClientsVehicles.get(index);
        }
        return null;
    }
    

    private ClientsVehiclesModel getListModelFromName(String searchName) {
        for (ClientsVehiclesModel model : listClientsVehicles) {
            if (searchName.contains(model.getObservacions())) {
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
            ClientsVehiclesModel selectedItem = listClientsVehicles.get(selectedEntry);
            view.itemObservationsField.setText(selectedItem.getObservacions());

            int categoryIndex = getCategoryIndexById(selectedItem.getIdVehicle());
            int clientIndex = getClientIndexById(selectedItem.getIdClient());

            view.categoryComboBox.setSelectedIndex(categoryIndex);
            view.clientComboBox.setSelectedIndex(clientIndex);
        } else {
            view.itemObservationsField.setText("");
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

    private String getClientNameById(int clientId) {
        for (ClientsModel client : listClients) {
            if (client.getId() == clientId) {
                return client.getNom();
            }
        }
        return "Cliente no encontrado"; // Manejar el caso en que el cliente no se encuentre
    }
    
    private String getVehicleBrandById(int vehicleId) {
        for (CategoriaModel category : listCategories) {
            if (category.getId() == vehicleId) {
                return category.getMarca() + " " + category.getModel();
            }
        }
        return "Marca no encontrada"; // Manejar el caso en que la marca no se encuentre
    }
    
    private String getRentalDatesForClientAndVehicle(int clientId, int vehicleId) {
        ArrayList<RentalModel> rentals = RentalDAO.getAll();
        for (RentalModel rental : rentals) {
            if (rental.getIdClient() == clientId && rental.getIdVehicle() == vehicleId) {
                return rental.getDataInici() + " / " + rental.getDataFinal();
            }
        }
        return "Sense dates";
    }
    
}
