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

    // Accions per iniciar el controlador
    public void start() {
        setupActionListeners();
        loadData();
        // No poseu més instruccions aquí,
        // perquè hem obert un fil en paral·lel a 'loadData'
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

        // Desactiva tots els elements i mostra el 'Carregant ...'
        setStatus(STATUS_LOADING);

        // Gestionar dades del DAO en una tasca paral·lela
        UtilsSwingThread.run(
            () -> {
                // Actualitzem la llista de categories a mostrar.
                list = ClientsDAO.getAll();

                // Simula espera
                Thread.sleep(1500);

                // Definir el adreca escollit
                if (!list.isEmpty()) {

                    // Mirar si hi ha alguna categoria escollida al 'comboBox'
                    int oldSelected = view.itemComboBox.getSelectedIndex();
                    int newSelected = 0;

                    // Si hi havia un adreca escollit, cal mirar quina posició té ara a la llista
                    if (oldSelected != -1) {
                        String selectedName = view.itemComboBox.getItemAt(oldSelected);
                        ClientsModel tmp = getListModelFromName(selectedName);
                        if (tmp != null) {
                            newSelected = list.indexOf(tmp);
                        }
                    }
                    
                    // Elimina tots els elements existents al JComboBox
                    view.itemComboBox.removeAllItems();

                    // Afegeix les noves categories rebudes al 'comboBox'
                    for (ClientsModel itemModel : list) {
                        view.itemComboBox.addItem(itemModel.getNom());
                    }

                    // Selecciona el adreca escollit al 'comboBox'
                    view.itemComboBox.setSelectedIndex(newSelected);
                }

                // Activa els elements i amaga 'Carregant ...'
                setStatus(oldStatus);
                fillFormData();
            }
        );
        // No posar més instruccions després del UtilsSwingThread.run
    }

    // Mostra o amaga els elements segons l'estat
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

    // Estableix l'acció del botó 'reload'
    private void controllerReloadButtonAction(ActionEvent e) {
        loadData();
        // 'loadData' crida a 'UtilsSwingThread.run'
        // No posar més instruccions després del UtilsSwingThread.run
    }

    // Quan s'apreta el 'checkBox' es posa en mode 'afegir' o 'modificar/esborrar'
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

    // Quan es canvia el valor del 'comboBox', es mostra el text sel·leccional al camp 'Nom'
    private void controllerItemComboBoxAction (ItemEvent e) {
        fillFormData();
    }

    // Estableix l'acció del botó 'afegir'
    private void controllerAddButtonAction(ActionEvent e) {
        String nom = view.itemNomField.getText();
        String adreca = view.itemAdrecaField.getText();
        int telefon = Integer.parseInt(view.itemTelefonField.getText());
        ClientsModel newModel = new ClientsModel(nom, adreca, telefon);

        setStatus(STATUS_MODIFY);

        // Gestionar dades del DAO en una tasca paral·lela
        UtilsSwingThread.run(
            () -> {
                ClientsDAO.addItem(newModel);

                // Actualitzar també el comboBox amb el nou nom 
                // (perquè al carregar les dades quedi seleccionat)
                int index = view.itemComboBox.getItemCount() - 1;
                view.itemComboBox.insertItemAt(nom, index);
                view.itemComboBox.setSelectedIndex(index);
                view.itemNomField.setText(nom);
                view.itemAdrecaField.setText(adreca);
                view.itemTelefonField.setText(String.valueOf(telefon));

        
                // Actualitzar les dades normalment
                loadData();
            }
        );
        // No posar més instruccions després del UtilsSwingThread.run
    }

    // Estableix l'acció del botó 'modificar'
    private void controllerModifyButtonAction(ActionEvent e) {
        String nom = view.itemNomField.getText();
        String adreca = view.itemAdrecaField.getText();
        int telefon = Integer.parseInt(view.itemTelefonField.getText());
        int index = view.itemComboBox.getSelectedIndex();

        // Crear una categoria amb les dades modificades
        ClientsModel modifiedModel = getModelFromComboBoxIndex(index);
        modifiedModel.setNom(nom);
        modifiedModel.setAdreca(adreca);
        modifiedModel.setTelefon(telefon);

        // Gestionar dades del DAO en una tasca paral·lela
        UtilsSwingThread.run(
            () -> {
                ClientsDAO.updateItem(modifiedModel);

                // Actualitzar també el comboBox amb el nou nom 
                // (perquè al carregar les dades quedi seleccionat)
                view.itemComboBox.removeItemAt(index);
                view.itemComboBox.insertItemAt(nom, index);
                view.itemComboBox.setSelectedIndex(index);
        
                // Actualitzar les dades normalment
                loadData();
            }
        );
        // No posar més instruccions després del UtilsSwingThread.run
    }

    // Estableix l'acció del botó 'borrar'
    private void controllerDeleteButtonAction(ActionEvent e) {
        int index = view.itemComboBox.getSelectedIndex();
        ClientsModel modifiedModel = getModelFromComboBoxIndex(index);

        // Gestionar dades del DAO en una tasca paral·lela
        UtilsSwingThread.run(
            () -> {
                ClientsDAO.deleteItem(modifiedModel.getId());

                // Actualitzar les dades normalment
                loadData();
            }
        );
        // No posar més instruccions després del UtilsSwingThread.run
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

    // Emplena els camps del formulari segons la sel·lecció de la comboBox
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