package com.ardat.moviecatalogue.setting

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceActivity
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatDelegate

open class AppCompatPref : PreferenceActivity() {


    private var mAppCompatDelegate: AppCompatDelegate? = null

    val supportActionBar: androidx.appcompat.app.ActionBar?
        get() = delegate!!.getSupportActionBar()

    private val delegate: AppCompatDelegate?
        get() {
            if (mAppCompatDelegate == null) {
                mAppCompatDelegate = AppCompatDelegate.create(this, null)
            }
            return mAppCompatDelegate
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate!!.installViewFactory()
        delegate!!.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun invalidateOptionsMenu() {
        delegate!!.invalidateOptionsMenu()
    }

    fun setSupportActionBar(@Nullable toolbar: Toolbar) {
        delegate!!.run { setSupportActionBar(toolbar) }
    }

    override fun getMenuInflater(): MenuInflater {
        return delegate!!.getMenuInflater()
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        delegate!!.setContentView(layoutResID)
    }

    override fun setContentView(view: View) {
        delegate!!.setContentView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        delegate!!.setContentView(view, params)
    }

    override fun addContentView(view: View, params: ViewGroup.LayoutParams) {
        delegate!!.addContentView(view, params)
    }

    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        delegate!!.setTitle(title)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delegate!!.onPostCreate(savedInstanceState)
    }

    override fun onPostResume() {
        super.onPostResume()
        delegate!!.onPostResume()
    }

    override fun onStop() {
        super.onStop()
        delegate!!.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        delegate!!.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        delegate!!.onDestroy()
    }
}
