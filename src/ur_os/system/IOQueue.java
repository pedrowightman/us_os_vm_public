/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.system;

import ur_os.process.Process;
import ur_os.process.ProcessState;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author pedro.wightman
 */
public class IOQueue {
    OS os;
    protected ArrayList<Process> processes;
    
    
    IOQueue(){
        processes = new ArrayList();
    }
   
    
    IOQueue(OS os){
        this();
        this.os = os;
    }
    
     public void setOS(OS os){
        this.os = os;
    }
    
    public void addProcess(Process p){
        p.setState(ProcessState.NEW_IO);   
        processes.add(p);
    }
    
    public void update(){
        Process temp;
        for (int i=0; i< processes.size(); i++) {
            if(processes.get(i).getState() != ProcessState.NEW_IO){
                if(processes.get(i).advanceInstruction()){//If the process finishes the current burst
                    temp = processes.get(i);
                    processes.remove(processes.get(i));
                    os.interrupt(InterruptType.IO_DONE, temp);
                    i--;
                }
            }else{
                processes.get(i).setState(ProcessState.IO);
            }
        }
    }
    
    public void removeProcess(Process p){
        processes.remove(p); //The process is found thanks to the equals() method
    }
    
    public String toString(){
        
        if(!processes.isEmpty()){
            StringBuilder sb = new StringBuilder();
            sb.append("IO: ");
            for (Process p : processes) {
                sb.append(p);
                sb.append("\n");
            }

            return sb.toString();
        
        }else
            return "IO: Empty";
    }
    
}
