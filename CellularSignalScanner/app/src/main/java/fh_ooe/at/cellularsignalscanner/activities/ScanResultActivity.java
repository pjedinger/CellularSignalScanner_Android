
package fh_ooe.at.cellularsignalscanner.activities;

import android.arch.persistence.room.Update;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.tasks.DeleteHistoryEntryTask;
import fh_ooe.at.cellularsignalscanner.tasks.GetHistoryEntryTask;
import fh_ooe.at.cellularsignalscanner.tasks.UpdateHistoryEntryNameTask;

public class ScanResultActivity extends AppCompatActivity {

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        id = getIntent().getExtras().getInt("id");
        String name = getIntent().getExtras().getString("name");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        GetHistoryEntryTask getHistoryEntryTask = new GetHistoryEntryTask(this);
        getHistoryEntryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanresult_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        } else if (item.getItemId() == R.id.scan_result_edit) {
            new LovelyTextInputDialog(this)
                    .setTopColorRes(R.color.colorPrimaryDark)
                    .setTitle(R.string.edit_scan_name_title)
                    .setMessage(R.string.edit_scan_name_text)
                    //.setIcon(R.drawable.ic_assignment_white_36dp)
                    .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                        @Override
                        public void onTextInputConfirmed(String text) {
                            if (text.length() != 0) {
                                UpdateHistoryEntryNameTask updateHistoryEntryNameTask = new UpdateHistoryEntryNameTask();
                                String[] strings = new String[2];
                                strings[0] = text;
                                strings[1] = id + "";
                                updateHistoryEntryNameTask.execute(strings);
                                ActionBar actionBar = getSupportActionBar();
                                actionBar.setTitle(text);
                            }
                        }
                    })
                    .setNegativeButton(R.string.delete_cancel, null)
                    .show();

        } else if (item.getItemId() == R.id.scan_result_delete) {
            //delete
            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                    .setTopColorRes(R.color.colorPrimaryDark)
                    .setButtonsColorRes(R.color.colorPrimaryDark)
                    .setTitle(R.string.delete_scan_title)
                    .setMessage(R.string.delete_scan_text)
                    .setPositiveButton(R.string.delete_yes, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeleteHistoryEntryTask deleteHistoryEntryTask = new DeleteHistoryEntryTask();
                            deleteHistoryEntryTask.execute(id);
                            onBackPressed();
                        }
                    })
                    .setNegativeButton(R.string.delete_cancel, null)
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }


}
