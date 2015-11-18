
package beans;

import javax.ejb.Remote;


/**
 * This is the business interface for Voting enterprise bean.
 */
@Remote
public interface VotingRemote
{
    void castVote(String candidate);
    
}
