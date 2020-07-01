package com.dongduo.library.inventory;

import com.dongduo.library.inventory.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryApplication extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        launch(InventoryApplication.class, MainView.class, args);
    }
}