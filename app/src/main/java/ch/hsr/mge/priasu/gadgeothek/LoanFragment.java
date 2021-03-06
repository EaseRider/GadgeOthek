package ch.hsr.mge.priasu.gadgeothek;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import ch.hsr.mge.gadgeothek.domain.Loan;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class LoanFragment extends Fragment implements AbsListView.OnItemClickListener {

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    private ProgressBar mProgressBar;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<Loan> mAdapter;

    public static LoanFragment newInstance() {
        LoanFragment fragment = new LoanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LoanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<Loan>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


        mProgressBar = (ProgressBar) view.findViewById(R.id.empty_progressbar);

        // TODO: Get Loans from Service
        mProgressBar.setVisibility(View.VISIBLE);

        setEmptyText(getString(R.string.loanList_label_noFound));
        LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                mProgressBar.setVisibility(View.GONE);
                mAdapter.clear();
                if (input.size() > 0) {
                    for (Loan loan : input)
                        mAdapter.insert(loan, mAdapter.getCount());
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
                setEmptyText(getString(R.string.loanList_label_noFound) + " " + message);
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
        // Nothing happens
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
}
