package com.insuranceline.utils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Set;

import timber.log.Timber;


public abstract class BaseFragmentStatePagerAdapter extends PagerAdapter {
	private static final String TAG = "FragmentStatePagerAdapter";
	private static final boolean DEBUG = false;
	private final FragmentManager mFragmentManager;
	private FragmentTransaction mCurTransaction = null;

	private ArrayList<Fragment.SavedState> mSavedState = new ArrayList<Fragment.SavedState>();
	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	private Fragment mCurrentPrimaryItem = null;

	public BaseFragmentStatePagerAdapter(FragmentManager fm) {
		this.mFragmentManager = fm;
	}

	public abstract Fragment getItem(int paramInt);

	public void startUpdate(ViewGroup container) {
	}

	public Object instantiateItem(ViewGroup container, int position) {
		if (this.mFragments.size() > position) {
			Fragment f = (Fragment) this.mFragments.get(position);
			if (f != null) {
				return f;
			}
		}

		if (this.mCurTransaction == null) {
			this.mCurTransaction = this.mFragmentManager.beginTransaction();
		}

		Fragment fragment = getItem(position);

		if (this.mSavedState.size() > position) {
			Fragment.SavedState fss = (Fragment.SavedState) this.mSavedState
					.get(position);
			if (fss != null) {
				fragment.setInitialSavedState(fss);
			}
		}
		while (this.mFragments.size() <= position) {
			this.mFragments.add(null);
		}
		fragment.setMenuVisibility(false);
		fragment.setUserVisibleHint(false);
		this.mFragments.set(position, fragment);
		
		this.mCurTransaction.add(container.getId(), fragment);

		return fragment;
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;

		if (this.mCurTransaction == null) {
			this.mCurTransaction = this.mFragmentManager.beginTransaction();
		}

		while (this.mSavedState.size() <= position) {
			this.mSavedState.add(null);
		}
		this.mSavedState.set(position,
				this.mFragmentManager.saveFragmentInstanceState(fragment));
		this.mFragments.set(position, null);
		
		this.mCurTransaction.remove(fragment);
	}

	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		if (fragment != this.mCurrentPrimaryItem) {
			if (this.mCurrentPrimaryItem != null) {
				this.mCurrentPrimaryItem.setMenuVisibility(false);
				this.mCurrentPrimaryItem.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			this.mCurrentPrimaryItem = fragment;
		}
	}

	public void finishUpdate(ViewGroup container) {
		if (this.mCurTransaction != null) {
			this.mCurTransaction.commitAllowingStateLoss();
			this.mCurTransaction = null;
			this.mFragmentManager.executePendingTransactions();
		}
	}

	public boolean isViewFromObject(View view, Object object) {
		return ((Fragment) object).getView() == view;
	}

	public Parcelable saveState() {
		Bundle state = null;
		if (this.mSavedState.size() > 0) {
			state = new Bundle();
			Fragment.SavedState[] fss = new Fragment.SavedState[this.mSavedState
					.size()];
			this.mSavedState.toArray(fss);
			state.putParcelableArray("states", fss);
		}
		for (int i = 0; i < this.mFragments.size(); i++) {
			Fragment f = (Fragment) this.mFragments.get(i);
			if (f != null) {
				if (state == null) {
					state = new Bundle();
				}
				String key = "f" + i;
				this.mFragmentManager.putFragment(state, key, f);
			}
		}
		return state;
	}

	public void restoreState(Parcelable state, ClassLoader loader) {
		Bundle bundle;
		if (state != null) {
			bundle = (Bundle) state;
			bundle.setClassLoader(loader);
			Parcelable[] fss = bundle.getParcelableArray("states");
			this.mSavedState.clear();
			this.mFragments.clear();
			if (fss != null) {
				for (int i = 0; i < fss.length; i++) {
					this.mSavedState.add((Fragment.SavedState) fss[i]);
				}
			}
			Set<String> keys = bundle.keySet();
			for (String key : keys)
				if (key.startsWith("f")) {
					int index = Integer.parseInt(key.substring(1));
					Fragment f;
					try {
						f = this.mFragmentManager.getFragment(bundle, key);
					} catch (Exception e) {
						f = null;
					}
					if (f != null) {
						while (this.mFragments.size() <= index) {
							this.mFragments.add(null);
						}
						f.setMenuVisibility(false);
						this.mFragments.set(index, f);
					} else {
						Timber.w("FragmentStatePagerAdapter Bad fragment at key %s", key);
					}
				}
		}
	}
}