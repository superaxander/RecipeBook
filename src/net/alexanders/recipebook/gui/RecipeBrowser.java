package net.alexanders.recipebook.gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RecipeBrowser{
    
    private JList RecipeList;
    private JEditorPane recipe;
    private JPanel mainPanel;
    private JScrollPane scroller;
    public static String[] recipes;
    public static String[] recipeNames;
    public static RecipeBrowser recipeBrowser = new RecipeBrowser();

    public static void main(String[] args){
        loadRecipes();
        JFrame frame = new JFrame("RecipeBrowser");
        frame.setContentPane(recipeBrowser.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recipeBrowser.RecipeList.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e){
                RecipeBrowser.changeRecipe((String)RecipeBrowser.recipeBrowser.RecipeList.getSelectedValue());
            }
        });
        DefaultListModel model = new DefaultListModel();
        for(String recipeName : recipeNames){
            model.addElement(recipeName);
        }
        recipeBrowser.RecipeList.setModel(model);
        recipeBrowser.recipe.addHyperlinkListener(new HyperlinkListener(){
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e){
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                if(Desktop.isDesktopSupported()){
                    try{
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    }catch(IOException|URISyntaxException ex){
                        System.out.println("Invalid URL");
                        ex.printStackTrace();
                    }
                }
            }
        });
        frame.pack();
        frame.setSize(1024, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void changeRecipe(String recipeName){
        for(int i = 0; i < recipeNames.length; i++){
            if(recipeName.equals(recipeNames[i])){
                recipeBrowser.recipe.setText(recipes[i]);
                recipeBrowser.recipe.setCaretPosition(0);
            }
        }
    }

    public static void loadRecipes(){
        ArrayList<File> recipeFiles = new ArrayList<File>();
        File folder = new File(System.getProperty("user.home")+"\\RecipeBook");
        if(!folder.exists()){
            if(!folder.mkdir()){
                System.out.println("Couldn't create folder.");
            }
        }else{
            try{
                for(File file : folder.listFiles()){
                    if(file.isFile()) recipeFiles.add(file); else System.out.println("Found directory in recipe directory. Ignoring.");
                }
            }catch(NullPointerException npe){
                System.out.println("The RecipeBook folder was empty please populate it.");
            }
        }
        recipeNames = new String[recipeFiles.size()];
        for(int i = 0; i < recipeFiles.size(); i++){
            File file = recipeFiles.get(i);
            recipeNames[i] = file.getName();
        }
        recipes = new String[recipeFiles.size()];
        for(int i = 0; i < recipeFiles.size(); i++){
            try{
                File file = recipeFiles.get(i);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                recipes[i] = loadRecipe(reader);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    public static String loadRecipe(BufferedReader bufferedReader){
        StringBuilder stringBuilder = new StringBuilder();
        bufferedReader.lines().forEach(line -> stringBuilder.append(line).append("\n"));
        return stringBuilder.toString();
    }
}
