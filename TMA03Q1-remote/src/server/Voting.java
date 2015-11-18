/*
 * Voting.java
 * 2008 - 2013
 */

package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 *
 * @author M362
 */
public interface Voting extends Remote
{
    public void recordVote(String candidate) throws RemoteException, InterruptedException;
    public Map<String, Integer> getVotes() throws RemoteException;
}