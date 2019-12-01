package rmit.ad.cleanup.dto;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class UserCleanupSite {
    private String userId;
    private String cleanupId;
    private Date joinDate;

    public UserCleanupSite(){}

    public UserCleanupSite(String userId, String cleanupId, Date joinDate){
        this.userId = userId;
        this.cleanupId = cleanupId;
        this.joinDate = joinDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCleanupId() {
        return cleanupId;
    }

    public void setCleanupId(String cleanupId) {
        this.cleanupId = cleanupId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
