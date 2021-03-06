package app.controllers.manager.crud.dialogs;

import app.controllers.FxmlController;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public interface ManagerDialogController extends FxmlController{

    void setDialogStage(Stage dialogStage);

    void setTableView(TableView tableView);

    <S> void setEditObject(S editObject);

    void setSelectedIndex(int index);

    boolean isInputValid();

    //<S> void removeObjectFromDB(S object);
}
