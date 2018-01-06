package app.controllers.manager.manager_dialogs;

import app.cores.StageManager;
import app.enums.ManagerEditDialogPath;
import app.enums.Pathable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Component
public class AddButton<S> {

    private static final String OBJECT_PATH = "app.entities.";
    private static final String MANAGE_EDIT_DIALOG = "MANAGE_%s_EDIT_DIALOG";

    private Button addButton;
    private StageManager stageManager;

    @Autowired
    @Lazy
    public AddButton(StageManager stageManager) {
        this.addButton = new Button();
        this.stageManager = stageManager;
    }


    public <S> Button createButton(String entityName, TableView genericTable) {
        buttonProperties(entityName);

        this.addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //getting the object which will be edited
                Class<?> objectClass = null;
                try {
                    objectClass = Class.forName(OBJECT_PATH + entityName);
                    Constructor<?> objectConstructor = objectClass.getDeclaredConstructor();
                    S newObject = (S) objectConstructor.newInstance();
                    String dialogPath = String.format(MANAGE_EDIT_DIALOG, newObject.getClass().getSimpleName().toUpperCase());
                    Pathable crudDialogPath = ManagerEditDialogPath.valueOf(dialogPath);
                    showProductEditDialog(newObject, crudDialogPath, genericTable);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                }
        });
        return this.addButton;
    }

    private <S> void showProductEditDialog(S editObject, Pathable viewPath, TableView genericTable){

        // try{

        Parent editDialogParent = stageManager.getPane(viewPath);
        Stage editDialog = new Stage();
        editDialog.initStyle(StageStyle.UNDECORATED);

        //pop up window must be closed to continue interaction with the program
        editDialog.initModality(Modality.APPLICATION_MODAL);
        //editDialog.initModality(Modality.WINDOW_MODAL);
        editDialog.setTitle("Add");

        //set scene
        Scene dialogScene = new Scene(editDialogParent);
        editDialog.setScene(dialogScene);

        ManagerDialogController controller = this.stageManager.getController();
        controller.setDialogStage(editDialog);
        controller.setEditObject(null);
        //controller.setProduct(editObject);
        controller.setTableView(genericTable);

        editDialog.showAndWait();

        // } catch (Exception e){
        //   System.out.println("ShowProductEditDialog in EditButtonCell class return false");
        //   }
    }


    private void buttonProperties(String entityName) {
        String buttonName = this.getClass().getSimpleName().replace("Button", "");
        this.addButton.getStyleClass().add("addButton");
        this.addButton.setText(String.format("ADD %s",entityName.toUpperCase()));
    }
}