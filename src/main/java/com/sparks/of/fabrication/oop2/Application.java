package com.sparks.of.fabrication.oop2;

import com.sparks.of.fabrication.oop2.models.RoleModel;
import com.sparks.of.fabrication.oop2.utils.SceneLoader;
import com.sparks.of.fabrication.oop2.users.Role;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Env;
import com.sparks.of.fabrication.oop2.utils.Singleton;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Main application class for initializing and launching the JavaFX application.
 */
public class Application extends javafx.application.Application {
    // Initialize environment, entity manager, logger, and scene loader as singletons
    private static final Env env = Singleton.getInstance(Env.class, new Env()).y();
    private static final EntityManagerWrapper entityManager =
            Singleton.getInstance(EntityManagerWrapper.class, new EntityManagerWrapper(env)).y();
    private static final Logger log = LogManager.getLogger(Application.class);
    private static final SceneLoader loader = Singleton.getInstance(SceneLoader.class, new SceneLoader()).y();

    /**
     * Starts the JavaFX application by loading the main scene.
     *
     * @param stage the primary stage for the application
     * @throws IOException if there is an error loading the FXML file
     * @throws NoSuchFieldException if there is an issue with the database schema
     */
    @Override
    public void start(Stage stage) throws IOException, NoSuchFieldException {
        loader.loadScene("scenes/main_scene.fxml", 450, 240, "Main", false, stage);
    }

    /**
     * Sets up the database by ensuring the roles are present.
     *
     * @throws NoSuchFieldException if there is an issue with accessing the role field
     */
    private static void setupDB() throws NoSuchFieldException {
        for (Role role : Role.values()) {
            if(!entityManager.findEntityByVal(RoleModel.class, RoleModel.class.getDeclaredField("role"), role).x()) {
                RoleModel roleModel = new RoleModel();
                roleModel.setRole(role);
                if(!entityManager.genEntity(roleModel)) {
                    log.error("Couldn't save to database! {}", roleModel.getId());
                    throw new RuntimeException("Couldn't save to database!");
                }
            }
        }
    }

    /**
     * Exits the application and cleans up the entity manager.
     */
    private static void exit() {
        boolean exit = entityManager.cleanUp();

        if(exit) {
            log.info("Application exited successfully");
            System.exit(0);
        }
        else {
            log.info("Application exited unsuccessfully");
            System.exit(1);
        }
    }

    /**
     * Main method to launch the JavaFX application and set up the database.
     *
     * @param args command-line arguments
     * @throws NoSuchFieldException if there is an issue setting up the database
     */
    public static void main(String[] args) throws NoSuchFieldException {
        launch();
        setupDB();
        exit();
    }
}
