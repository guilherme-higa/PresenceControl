package com.example.presencecontroltestapp.provider;

import com.example.presencecontroltestapp.entities.Students;

public interface IDatabaseResult {
        void onConnectionResponse(boolean result);
        void onSelectResponse(boolean result);
        void onSelectResponse(boolean result, Students students);
//    void onInsertResponse(boolean result);
//    void onDeleteResponse(boolean result);
//    void onUpdateResponse(boolean result);


    interface Recovery {
        void onRecoverySelect(boolean result);
    }

    interface CreateAccount {
        void onCreateAccountSelect(boolean result);
    }

}

