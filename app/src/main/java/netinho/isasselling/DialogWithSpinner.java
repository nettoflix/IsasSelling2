package netinho.isasselling;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import netinho.isasselling.Manager.Handler;
import netinho.isasselling.Manager.MyMath;

public class DialogWithSpinner extends AppCompatDialogFragment {
    DialogListener parent;
    View dialogLayout;
    Produto product;
    String title;
    String message;

    public DialogWithSpinner(DialogListener parent, Activity activity, Produto product, String title, String message) {
        this.title = title;
        this.message = message;
        this.product = product;
        dialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_product_remove, null);
        this.parent = parent;
    }


    AlertDialog.Builder alertDialog;

    public Dialog onCreateDialog(Bundle savedInstance) {
        alertDialog = Handler.setAlertDialog(getActivity(), title, message);
        alertDialog.setView(dialogLayout);
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                parent.onClickDialog(product);
            }
        });
        alertDialog.setNegativeButton(android.R.string.no, null);
        return alertDialog.create();
    }

public interface DialogListener
{
    void onClickDialog(Produto product);
}
}
