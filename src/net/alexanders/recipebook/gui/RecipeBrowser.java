package net.alexanders.recipebook.gui;

import net.alexanders.recipebook.*;

import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class RecipeBrowser{
    
    private JList RecipeList;
    private JEditorPane recept;
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
        frame.pack();
        frame.setSize(1024, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void changeRecipe(String recipeName){
        Stream stream = Arrays.stream(recipeNames).filter(recipeName::equals);
        if(stream.count() == 1){

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
        recipeNames = new String[recipeFiles.size()-1];
        for(int i = 0; i < recipeFiles.size(); i++){
            File file = recipeFiles.get(i);
            recipeNames[i] = file.getName().substring(1, file.getName().length() - 3);
        }
        for(int i = 0; i < recipeFiles.size(); i++){
            try{
                recipes[i] = loadRecipe(new BufferedReader(new FileReader(recipeFiles.get(i))));
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
