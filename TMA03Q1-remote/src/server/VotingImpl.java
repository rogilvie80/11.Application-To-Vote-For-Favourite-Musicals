/*
 * VotingImpl.java
 * 2008 - 2013
 */

package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author M362
 */
public class VotingImpl extends UnicastRemoteObject implements Voting
{
   // map to store the votes
   Map<String, Integer> votes;
   
   public VotingImpl() throws RemoteException
   {
      // thread safe map
      votes = new ConcurrentHashMap<String, Integer>();
      // add some sample musicals
      votes.put("Les Miserables", 0);
      votes.put("Mary Poppins", 0);
      votes.put("The Sound of Music", 0);
      votes.put("West Side Story", 0);
   }
   
   public void recordVote(String candidate) throws RemoteException
   {
      votes.put(candidate, votes.get(candidate) + 1);
   }
   
   public Map<String, Integer> getVotes() throws RemoteException
   {
      return new TreeMap<String, Integer>(votes);
   }
}