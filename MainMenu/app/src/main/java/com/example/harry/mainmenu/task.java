package com.example.harry.mainmenu;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Harry on 02/03/2018.
 */

public class task implements Serializable{ //this class will store the information about the tasks

    private String taskName = null;
    private String instructions = null;
    private ArrayList<task> dependencies = null;
    private ArrayList<Integer> timeValues = null;

    //need to add dependencies
    //need to add how long the task will take

    public String getTaskName(){
        return taskName;
    }

    public String getInstructions(){
        return instructions;
    }

    public ArrayList<task> getDependencies(){return dependencies;}

    public void setTaskName(String name){
        taskName = name;
    }

    public void setInstructions(String newInstructions){
        instructions = newInstructions;
    }

    public void setDependencies(ArrayList<task> newDep){ dependencies = newDep;}

    public ArrayList<Integer> getTimeValues(){return  timeValues;}

    public void setTimeValues(ArrayList<Integer> newTime){
        timeValues = newTime;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;

        if(!(obj instanceof task)) return false;

        task passedTask = (task)obj;

        return taskName.equals(passedTask.getTaskName()) && instructions.equals(passedTask.getInstructions()) && dependencies.equals(passedTask.getDependencies()) && timeValues.equals(passedTask.getTimeValues());
    }

}
