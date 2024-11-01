/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os.process.planning;

import java.util.ArrayList;
import ur_os.process.Process;
import ur_os.system.OS;

/**
 *
 * @author super
 */
public class ReadyQueue {
    
    Scheduler s;
    OS os;
    
    
    public ReadyQueue(OS os){
        this.os = os;
        
        //s = new SJF_P(os);
        
        //s = new SJF_NP(os);
        
        s = new FCFS(os);
        
        //s = new RoundRobin(os,6);
        
        //s = new MFQ(os,new RoundRobin(os,3),new RoundRobin(os,6),new FCFS(os));
        
        //s = new PriorityQueue(os,new RoundRobin(os,9,false),new RoundRobin(os,6,false),new RoundRobin(os,3,false),new RoundRobin(os,2,false));
    }
    
    public ReadyQueue(OS OS, Scheduler s){
        this.os = os;
        this.s = s;
    }
    
    public void addProcess(Process p){
        s.addProcess(p);
    }
    
    public Process removeProcess(Process p){
        return s.removeProcess(p);
    }
    
    public void update(){
        s.update();
    }
        
    public String toString(){
        
        return s.toString();
    }
    
   
    
}
