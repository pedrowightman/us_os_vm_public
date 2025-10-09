/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.virtualmemory;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author user
 */
public class PVMM_LRU extends ProcessVirtualMemoryManager{

    public PVMM_LRU(){
        type = ProcessVirtualMemoryManagerType.LRU;
    }
    
    @Override
    public int getVictim(LinkedList<Integer> memoryAccesses, ArrayList<Integer> validList) {
        LinkedList<Integer> pages = new LinkedList();
        int size = memoryAccesses.size()-1;
        int loaded = validList.size();
        int temp;
        while(size >= 0 && pages.size()<loaded){
            temp = memoryAccesses.get(size);
            if(!pages.contains(temp) && validList.contains(temp)){
                pages.add(memoryAccesses.get(size));
            }
            size--;
        }
        
        return pages.getLast();
    }
    
}
