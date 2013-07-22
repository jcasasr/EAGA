/*
 * Copyright 2013 Jordi Casas-Roma, Alexandre Dotor Casals
 * 
 * This file is part of UMGA. 
 * 
 * UMGA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UMGA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UMGA.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.uoc.kison.EAGA.utils;

import org.apache.log4j.Logger;

/**
 *
 * @author jcasasr
 */
public class Memory {
    
    private static final Logger logger = Logger.getLogger(Memory.class);
    
    public Memory() {
        super();
    }

    public long test() {

        int mb = 1024 * 1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
        
        long used = (runtime.totalMemory() - runtime.freeMemory()) / mb;
        long free = runtime.freeMemory() / mb;
        long total = runtime.totalMemory() / mb;
        long max = runtime.maxMemory() / mb;

        logger.info(String.format("Heap memory usage: %d Used / %d Free / %d Total / %d Max [MB]", used, free, total, max));
        
        return used;
    }
    
    public long testKb() {

        int kb = 1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
        
        long used = (runtime.totalMemory() - runtime.freeMemory()) / kb;
        long free = runtime.freeMemory() / kb;
        long total = runtime.totalMemory() / kb;
        long max = runtime.maxMemory() / kb;

        logger.info(String.format("Heap memory usage: %d Used / %d Free / %d Total / %d Max [KB]", used, free, total, max));
        
        return used;
    }
}
