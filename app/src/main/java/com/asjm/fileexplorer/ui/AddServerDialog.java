package com.asjm.fileexplorer.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.lib.util.ALog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.base.Strings;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddServerDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private Listener mListener;
    private TextInputLayout nameEditText;
    private TextInputLayout ipEditText;
    private TextInputLayout usernameEditText;
    private TextInputLayout passwordEditText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ALog.getInstance().d("onAttach" + context);
        mListener = ((Listener) context);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_server, null);
        nameEditText = view.findViewById(R.id.name);
        ipEditText = view.findViewById(R.id.ip);
        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);

        usernameEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updatePasswordStatus(s);
            }
        });
        updatePasswordStatus(null);

        builder.setTitle(R.string.title_add_server)
                .setView(view)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this);

        return builder.create();
    }

    private void updatePasswordStatus(Editable s) {
        final boolean hasUsername = !TextUtils.isEmpty(s);
        passwordEditText.getEditText().setEnabled(hasUsername);
        passwordEditText.setHint(getString(hasUsername ? R.string.hint_password : R.string.hint_anonymous));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                dialog.dismiss();
                Server server = buildServerInfo();
                mListener.onAdd(server);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                mListener.onDismiss();
                break;
            default:
                throw new IllegalArgumentException("unknown button: " + which);
        }
    }

    private Server buildServerInfo() {
        final String name = nameEditText.getEditText().getText().toString();
        final String ip = ipEditText.getEditText().getText().toString();
        final String username = usernameEditText.getEditText().getText().toString();
        final String password = passwordEditText.getEditText().getText().toString();
        Server server = new Server();
        server.setAddress(ip);
        server.setName(name);
        server.setUsername(username);
        server.setPassword(password);
        return server;
    }

    public interface Listener {
        void onDismiss();

        void onAdd(Server server);
    }
}
