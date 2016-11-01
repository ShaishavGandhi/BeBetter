package shaishav.com.bebetter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import shaishav.com.bebetter.R;

/**
 * Created by shaishavgandhi05 on 10/30/16.
 */

public class TagDialog extends Dialog implements TextWatcher{

    AutoCompleteTextView mCategoryTextView;

    public TagDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_dialog);
        mCategoryTextView = (AutoCompleteTextView) findViewById(R.id.categoryAutocomplete);

        mCategoryTextView.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
