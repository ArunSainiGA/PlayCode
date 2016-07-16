package com.cycus.playcodeapp.ModelManagers;

import com.cycus.playcodeapp.SetterGetter.CategoryBean;

/**
 * Created by Arun_Saini on 18-06-2016.
 */
public class ModelManager {
    private static ModelManager manager;
    private HomeManager homeManager;
    private CategoryManager categoryManager;
    private GameDescriptionManager gameDescriptionManager;
    private CategoryManagerGameDesc categoryManagerGameDesc;

    private ModelManager() {
        homeManager = new HomeManager();
        categoryManager = new CategoryManager();
        gameDescriptionManager = new GameDescriptionManager();
        categoryManagerGameDesc = new CategoryManagerGameDesc();
    }

    public static ModelManager getManager() {
        if (manager == null) {
            manager = new ModelManager();
        }
        return manager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public GameDescriptionManager getGameDescriptionManager() {
        return gameDescriptionManager;
    }

    public CategoryManagerGameDesc getCategoryManagerGameDesc() {
        return categoryManagerGameDesc;
    }
}
