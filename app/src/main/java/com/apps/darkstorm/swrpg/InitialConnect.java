package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

public class InitialConnect {
    public static void connect(Context main, GoogleApiClient gac){
        SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        DriveFolder root = Drive.DriveApi.getRootFolder(gac);
        DriveApi.MetadataBufferResult metBufRes = root.queryChildren(gac,new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE,"SWChars"))
                .addFilter(Filters.eq(SearchableField.MIME_TYPE,DriveFolder.MIME_TYPE)).build()).await();
        MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
        DriveId charsFold = null;
        for (Metadata met:metBuf){
            if (met.isFolder() && met.getTitle().equals("SWChars") && !met.isTrashed()){
                charsFold = met.getDriveId();
                break;
            }
        }
        metBuf.release();
        metBufRes.release();
        if (charsFold == null){
            DriveFolder.DriveFolderResult foldRes = root.createFolder(gac,new MetadataChangeSet.Builder().setTitle("SWChars").build()).await();
            charsFold = foldRes.getDriveFolder().getDriveId();
        }
        pref.edit().putString(main.getString(R.string.swchars_id_key),charsFold.encodeToString()).apply();
        DriveFolder fold = charsFold.asDriveFolder();
        metBufRes = fold.queryChildren(gac,new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE,"SWShips"))
                .addFilter(Filters.eq(SearchableField.MIME_TYPE,DriveFolder.MIME_TYPE)).build()).await();
        metBuf = metBufRes.getMetadataBuffer();
        DriveId shipsFold = null;
        for (Metadata met:metBuf){
            if (met.isFolder() && met.getTitle().equals("SWShips") && !met.isTrashed()){
                shipsFold = met.getDriveId();
                break;
            }
        }
        metBuf.release();
        metBufRes.release();
        if (shipsFold == null){
            DriveFolder.DriveFolderResult foldRes = fold.createFolder(gac,new MetadataChangeSet.Builder().setTitle("SWShips").build()).await();
            shipsFold = foldRes.getDriveFolder().getDriveId();
        }
        pref.edit().putString(main.getString(R.string.ships_id_key),shipsFold.encodeToString()).apply();
    }
}
