package ch.hsr.mge.priasu.gadgeothek;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Gadget;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;
import ch.hsr.mge.priasu.gadgeothek.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class BibliothekFragment extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    private ProgressBar mProgressBar;

    private TextView mErrorText;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<Gadget> mAdapter;

    public static BibliothekFragment newInstance() {
        BibliothekFragment fragment = new BibliothekFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BibliothekFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<Gadget>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bibliothek, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mErrorText = (TextView) view.findViewById(R.id.res_errorText);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.empty_progressbar);

        mProgressBar.setVisibility(View.VISIBLE);

        setEmptyText(getString(R.string.gadgetList_label_noFound));
        LibraryService.getGadgets(new Callback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                mProgressBar.setVisibility(View.GONE);
                mAdapter.clear();
                if (input.size() > 0) {
                    Gadget g = null;
                    for (Gadget gadget : input)
                        mAdapter.insert(gadget, mAdapter.getCount());
                } else {
                    setEmptyText(getString(R.string.gadgetList_label_noFound));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
                setEmptyText(getString(R.string.gadgetList_label_noFound) + " " + message);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return onLongListItemClick(view, position, id);
    }
    protected boolean onLongListItemClick(View v, final int pos, long id) {
        final Gadget pGadget = mAdapter.getItem(pos);
        Log.i("ListView", "onLongListItemClick Reservation=" + pGadget.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        mErrorText.setText("");
        builder.setMessage(getString(R.string.bibliothekList_label_addQuestion).replace("=gadget=", pGadget.getName()))
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LibraryService.reserveGadget(pGadget, new Callback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                if (!input)
                                    mErrorText.setText(getString(R.string.biblio_label_allready_3));
                            }

                            @Override
                            public void onError(String message) {
                                mErrorText.setText(message);
                            }
                        });
                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
}
