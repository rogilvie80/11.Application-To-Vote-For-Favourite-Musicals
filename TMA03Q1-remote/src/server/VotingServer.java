/*
 * VotingServer.java
 * 2008 - 2013
 */

package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.Scanner;

/**
 *
 * @author M362
 */
public class VotingServer
{
    
    Voting poll;
    Registry registry;
    
    public VotingServer()
    {
        try
        {
            // Create a registry for our server
            registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            
            // Create and bind an object
            poll = new VotingImpl();
            registry.bind("poll", poll);
            // Feedback to user
            System.out.println("Poll Server running...");
            System.out.println("Enter any non-empty string to stop");
        }
        catch (ExportException e)
        {
            System.out.println("Registry port already in use: cannot run second server " );
            System.out.println(e);
            System.exit(1);
        }
        catch (Exception e)
        {
            System.out.println("Could not bind object");
            e.printStackTrace();
        }
        
        // Mechanism to stop the server in an orderly manner
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            if (scanner.hasNext())
            {
                System.out.println("Server stopping.");
                System.exit(0);
            }
        }
    }
    
    public static void main(String [] args)
    {
        new VotingServer();
    }
}