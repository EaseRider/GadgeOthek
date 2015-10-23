package ch.hsr.mge.priasu.gadgeothek;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Reservation;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class ReservierungFragment extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ReservationListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private TextView mErrorText;

    private ProgressBar mProgressBar;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<Reservation> mAdapter = null;
    private int mSelectedItem = -1;
    private class ArrayAdapterDecorator extends ArrayAdapter<Reservation> {

        public ArrayAdapterDecorator(Context context, int resource) {
            super(context, resource);
        }

        public ArrayAdapterDecorator(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public ArrayAdapterDecorator(Context context, int resource, int textViewResourceId, Reservation[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        // A miserable try to Mark Item for deletion (Like Blue + bin-Button)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //mAdapter = new ArrayAdapter<Reservation>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
            final View view = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);

            if (position == mSelectedItem) {
                view.setBackgroundColor(3892978);
            }

            return super.getView(position, convertView, parent);
        }
    }


    // TODO: Rename and change types of parameters
    public static ReservierungFragment newInstance() {
        ReservierungFragment fragment = new ReservierungFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReservierungFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapterDecorator(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservierung, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        //mListView.setListView(mAdapter);

        mErrorText = (TextView) view.findViewById(R.id.res_errorText);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.empty_progressbar);

        // TODO: Get Loans from Service
        mProgressBar.setVisibility(View.VISIBLE);

        setEmptyText(getString(R.string.reservierungsList_label_noFound));

        getReservatons();

        return view;
    }

    public void getReservatons() {
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                mProgressBar.setVisibility(View.GONE);
                mAdapter.clear();
                if (input.size() > 0) {
                    for (Reservation reserv : input)
                        mAdapter.insert(reserv, mAdapter.getCount());
                } else {
                    setEmptyText(getString(R.string.loanList_label_noFound));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
                setEmptyText(getString(R.string.reservierungsList_label_noFound)+ " " + message);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ReservationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListView.setSelection(position);
        mListView.setItemChecked(position, true);
        mErrorText.setText("");
//        mSelectedItem = position;
        if (null != mListener) {
            mListener.onEditReservation(mAdapter.getItem(position));
        }
        mAdapter.notifyDataSetChanged();
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
        final Reservation pReservation = mAdapter.getItem(pos);
        Log.i("ListView", "onLongListItemClick Reservation=" + pReservation.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.reservierungsList_label_deleteQuestion).replace("=gadget=", pReservation.getGadget().getName()))
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LibraryService.deleteReservation(pReservation, new Callback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                getReservatons();
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



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ReservationListener {
        // TODO: Update argument type and name
        public void onAddReservation();
        public void onEditReservation(Reservation reservation);
    }

}
