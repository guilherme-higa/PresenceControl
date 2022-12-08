package com.example.presencecontroltestapp.provider;

import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.ui.fragments.ClassInformation;

import java.util.List;

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

    interface RecoveryFrequency {
        void onRecoveryFrequency(boolean result, ClassInformation classInformation);
        void onRecoveryFrequency(boolean result);
    }

    interface informationClass {
        void onClassInformationSelect(boolean result, List<ClassInformation> classInformation);
        void onClassInformationSelect(boolean result);
    }

    interface CreateAccount {
        void onCreateAccountSelect(boolean result);
    }

}

