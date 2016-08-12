package com.walmart.main;

import com.walmart.ticketservice.TicketServiceInitialize;

/*
 * This is a main class which will start the application
 * 
 * The application have four choices and developer can use a choice of his interest
 */
public class TicketServiceMain {
	public static void main(String args[]){
		TicketServiceInitialize.initialize(); // This function will initialize the data structures required for the application
		/*
		 * This choice will run the application with atomic operation for the user. 
		 * The user will complete the operation successfully or will be denied at the entry point
		 */
		if(args[0].equalsIgnoreCase("TicketServiceAtomic")){
			System.out.println("In TicketService Atomic");
			TicketServiceThread ticketServiceForAkarsh = new TicketServiceThread("Akarsh",1,2,1200,"akarsh.cholaveti@gmail.com");
			TicketServiceThread ticketServiceForASU = new TicketServiceThread("ASU",1,2,800,"acholave@asu.edu");
			TicketServiceThread ticketServiceForWalmart = new TicketServiceThread("Walamrt",1,3,1000,"walmart.com");
			
			Thread thread1=new Thread(ticketServiceForAkarsh);
			Thread thread2=new Thread(ticketServiceForASU);
			Thread thread3=new Thread(ticketServiceForWalmart);
			
			thread1.start();
			thread2.start();
			thread3.start();
		}
		/*
		 * This choice will run the application with atomic operation for the user
		 * The user will complete the operation successfully or will be denied at the entry point.
		 * The seat hold clear function will be called periodically by the action listener and it will clean the cache
		 */
		else if(args[0].equalsIgnoreCase("TicketServiceAtomicWithPeriodicCache")){
		
			TicketServiceThreadCache ticketServiceForAkarsh = new TicketServiceThreadCache("Akarsh",1,2,1200,"akarsh.cholaveti@gmail.com");
			TicketServiceThreadCache ticketServiceForASU = new TicketServiceThreadCache("ASU",1,2,800,"acholave@asu.edu");
			TicketServiceThreadCache ticketServiceForWalmart = new TicketServiceThreadCache("Walmart",1,3,1000,"walmart.com");
			CacheCleanThread cleanCache = new CacheCleanThread();
			
			Thread thread1=new Thread(ticketServiceForAkarsh);
			Thread thread2=new Thread(ticketServiceForASU);
			Thread thread3=new Thread(ticketServiceForWalmart);
			Thread thread4=new Thread(cleanCache); //This thread will clean the cache periodically
			
			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
		}/*
		  * This choice will run the application in parallel mode and the seat hold booking is 
		  * confirmed by the person who gets the seat first and other person will get the other seat (if available)
		  * other wise it will be notified about the missing seats for the user 
		  */
		else if(args[0].equalsIgnoreCase("TicketServiceParallel")){
			TicketServiceThreadParallel ticketServiceForAkarsh = new TicketServiceThreadParallel("Akarsh",1,2,1200,"akarsh.cholaveti@gmail.com");
			TicketServiceThreadParallel ticketServiceForASU = new TicketServiceThreadParallel("ASU",1,2,800,"acholave@asu.edu");
			TicketServiceThreadParallel ticketServiceForWalmart = new TicketServiceThreadParallel("Walamrt",1,3,1000,"walmart.com");
			
			Thread thread1=new Thread(ticketServiceForAkarsh);
			Thread thread2=new Thread(ticketServiceForASU);
			Thread thread3=new Thread(ticketServiceForWalmart);
			
			thread1.start();
			thread2.start();
			thread3.start();
		}/*
		  *This choice will run the application in parallel mode and the seat hold booking is 
		  * confirmed by the person who gets the seat first and other person will get the other seat (if available)
		  * other wise it will be notified about the missing seats for the user
		  * The seat hold clear function will be called periodically by the action listener and it will clean the cache
		  */
		else if(args[0].equalsIgnoreCase("TicketServiceParallelWithPeriodicCache")){

			TicketServiceThreadParallelCache ticketServiceForAkarsh = new TicketServiceThreadParallelCache("Akarsh",1,2,1200,"akarsh.cholaveti@gmail.com");
			TicketServiceThreadParallelCache ticketServiceForASU = new TicketServiceThreadParallelCache("ASU",1,2,800,"acholave@asu.edu");
			TicketServiceThreadParallelCache ticketServiceForWalmart = new TicketServiceThreadParallelCache("Walmart",1,3,1000,"walmart.com");
			CacheCleanThread cleanCache = new CacheCleanThread();
			
			Thread thread1=new Thread(ticketServiceForAkarsh);
			Thread thread2=new Thread(ticketServiceForASU);
			Thread thread3=new Thread(ticketServiceForWalmart);
			Thread thread4=new Thread(cleanCache);//This thread will clean the cache periodically
			
			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
		}
		else{
			System.out.println("Rerun the program with the correct code");
		}
	}
}
