package com.project;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

import javax.swing.JTabbedPane;

import com.project.UtilsSwingThread;

public class ClientsController {

    final private int STATUS_LOADING = 0;
    final private int STATUS_ADD = 1;
    final private int STATUS_MODIFY = 2;
    private int status = STATUS_ADD;

    private ClientsView view;
    private ArrayList<ClientsModel> list;
    private JTabbedPane tabbedPane;

    ClientsController(ClientsView view, JTabbedPane tabbedPane) {
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
    }

    public void loadData() {

        int oldStatus = status;
        setStatus(STATUS_LOADING);
        UtilsSwingThread.run(
            () -> {
                list = ClientsDAO.getAll();
                Thread.sleep(1500);
                if (!list.isEmpty()) {
                    int oldSelected = view.itemComboBox.getSelectedIndex();
                    int newSelected = 0;
                    if (oldSelected != -1) {
                        String selectedName = view.itemComboBox.getItemAt(oldSelected);
                        ClientsModel tmp = getListModelFromName(selectedName);
                        if (tmp != null) {
                            newSelected = list.indexOf(tmp);
                        }
                    }
                    view.itemComboBox.removeAllItems();
                    for (ClientsModel itemModel : list) {
                        view.itemComboBox.addItem(itemModel.getNom());
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
        view.itemNomField.setEnabled(!isLoading);
        view.itemAdrecaField.setEnabled(!isLoading);
        view.itemTelefonField.setEnabled(!isLoading);
        view.addButton.setEnabled(!isLoading && status == STATUS_ADD);
        view.modifyButton.setEnabled(!isLoading && status == STATUS_MODIFY);
        view.deleteButton.setEnabled(!isLoading && status == STATUS_MODIFY);
    }
    private void controllerReloadButtonAction(ActionEvent e) {
        loadData();
    }
    private void controllerNewItemCheckBoxAction (ItemEvent evt) {
        boolean isSelected = (evt.getStateChange() == ItemEvent.SELECTED);
        if (isSelected) {
            setStatus(STATUS_ADD);
            view.itemComboBox.setEnabled(false);
        } else {
            setStatus(STATUS_MODIFY);
            view.itemComboBox.setEnabled(true);
        }
        fillFormData();
    }
    private void controllerItemComboBoxAction (ItemEvent e) {
        fillFormData();
    }
    private void controllerAddButtonAction(ActionEvent e) {
        String nom = view.itemNomField.getText();
        String adreca = view.itemAdrecaField.getText();
        int telefon = Integer.parseInt(view.itemTelefonField.getText());
        ClientsModel newModel = new ClientsModel(nom, adreca, telefon);

        setStatus(STATUS_MODIFY);
        UtilsSwingThread.run(
            () -> {
                ClientsDAO.addItem(newModel);
                int index = view.itemComboBox.getItemCount() - 1;
                view.itemComboBox.insertItemAt(nom, index);
                view.itemComboBox.setSelectedIndex(index);
                view.itemNomField.setText(nom);
                view.itemAdrecaField.setText(adreca);
                view.itemTelefonField.setText(String.valueOf(telefon));
                loadData();
            }
        );
    }
    private void controllerModifyButtonAction(ActionEvent e) {
        String nom = view.itemNomField.getText();
        String adreca = view.itemAdrecaField.getText();
        int telefon = Integer.parseInt(view.itemTelefonField.getText());
        int index = view.itemComboBox.getSelectedIndex();
        ClientsModel modifiedModel = getModelFromComboBoxIndex(index);
        modifiedModel.setNom(nom);
        modifiedModel.setAdreca(adreca);
        modifiedModel.setTelefon(telefon);
        UtilsSwingThread.run(
            () -> {
                ClientsDAO.updateItem(modifiedModel);
                view.itemComboBox.removeItemAt(index);
                view.itemComboBox.insertItemAt(nom, index);
                view.itemComboBox.setSelectedIndex(index);
                loadData();
            }
        );
    }
    private void controllerDeleteButtonAction(ActionEvent e) {
        int index = view.itemComboBox.getSelectedIndex();
        ClientsModel modifiedModel = getModelFromComboBoxIndex(index);
        UtilsSwingThread.run(
            () -> {
                ClientsDAO.deleteItem(modifiedModel.getId());
                loadData();
            }
        );
    }

    private ClientsModel getModelFromComboBoxIndex(int index) {
        String comboBoxText = view.itemComboBox.getItemAt(index);
        return getListModelFromName(comboBoxText);
    }

    private ClientsModel getListModelFromName(String searchName) {
        ClientsModel rst = null;
        for (int i=0; i<list.size(); i++) {
            String listName = list.get(i).getNom();
            if (searchName.compareTo(listName) == 0) {
                rst = list.get(i);
                break;
            }
        }
        return rst;
    }
    private void fillFormData () {
        int selectedEntry = view.itemComboBox.getSelectedIndex();
        if (selectedEntry != -1 && status == STATUS_MODIFY) {
            view.itemNomField.setText(list.get(selectedEntry).getNom());
            view.itemAdrecaField.setText(list.get(selectedEntry).getAdreca());
            view.itemTelefonField.setText(String.valueOf(list.get(selectedEntry).getTelefon()));
        } else {
            view.itemNomField.setText("");
            view.itemAdrecaField.setText("");
            view.itemTelefonField.setText("");
        }
    }
}