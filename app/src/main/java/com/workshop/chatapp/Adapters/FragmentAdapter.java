package com.workshop.chatapp.Adapters;

import android.icu.text.CaseMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.workshop.chatapp.Fragment.CallsFragment;
import com.workshop.chatapp.Fragment.ChatsFragment;
import com.workshop.chatapp.Fragment.GroupsFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new ChatsFragment();
            case 1: return new GroupsFragment();
            case 2: return new CallsFragment();
            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        switch(position){
            case 0: {title="CHATS"; break;}
            case 1: {title="GROUPS"; break;}
            case 2: {title="CALLS"; break;}
            default: title="CHATS";
        }
        return title;
    }
}
