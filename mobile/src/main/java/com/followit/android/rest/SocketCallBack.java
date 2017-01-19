package com.followit.android.rest;

import com.followit.android.Node;

import org.json.JSONException;

import java.util.ArrayList;

public interface SocketCallBack {
    void onPathFetched(ArrayList<Node> path) throws JSONException;
    void onBroadcastNotification(String message);
    void POIListNotification(ArrayList<String> list);
}
