package com.iterable.iterableapi;

import androidx.annotation.RestrictTo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class IterableInboxSession {
    public final Date sessionStartTime;
    public final Date sessionEndTime;
    public final int startTotalMessageCount;
    public final int startUnreadMessageCount;
    public final int endTotalMessageCount;
    public final int endUnreadMessageCount;
    public final List<Impression> impressions;
    public final String sessionId;

    public IterableInboxSession(Date sessionStartTime, Date sessionEndTime, int startTotalMessageCount, int startUnreadMessageCount, int endTotalMessageCount, int endUnreadMessageCount, List<Impression> impressions) {
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
        this.startTotalMessageCount = startTotalMessageCount;
        this.startUnreadMessageCount = startUnreadMessageCount;
        this.endTotalMessageCount = endTotalMessageCount;
        this.endUnreadMessageCount = endUnreadMessageCount;
        this.impressions = impressions;
        this.sessionId = UUID.randomUUID().toString();
    }

    public IterableInboxSession() {
        this.sessionStartTime = null;
        this.sessionEndTime = null;
        this.startTotalMessageCount = 0;
        this.startUnreadMessageCount = 0;
        this.endTotalMessageCount = 0;
        this.endUnreadMessageCount = 0;
        this.impressions = null;
        this.sessionId = UUID.randomUUID().toString();
    }

    public static class Impression {
        final String messageId;
        final boolean silentInbox;
        final int displayCount;
        final float duration;

        public Impression(String messageId, boolean silentInbox, int displayCount, float duration) {
            this.messageId = messageId;
            this.silentInbox = silentInbox;
            this.displayCount = displayCount;
            this.duration = duration;
        }
    }
}