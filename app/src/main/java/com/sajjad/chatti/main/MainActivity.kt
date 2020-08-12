package com.sajjad.chatti.main

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.sajjad.application_component.ApplicationComponentProvider
import com.sajjad.base.presentation.observe
import com.sajjad.base.session.SessionHolder
import com.sajjad.chatti.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.nav_header_main.*
import javax.inject.Inject

internal class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    @Inject
    lateinit var sessionHolder: SessionHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDeps()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.navController = findNavController(R.id.navHostFragment)
        setUpNavigationWithAppbar()
        setUpNavigationWithDrawer()
        observeSession()
        setNavHeaderClickListener()
    }

    private fun injectDeps() {
        DaggerMainComponent.factory()
            .create(
                (application as ApplicationComponentProvider)
                    .applicationComponent.provideSessionComponent()
            )
            .inject(this)
    }

    private fun setUpNavigationWithAppbar() {
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.run {
                if (destination.id == R.id.loginFragment || destination.id == R.id.signUpFormFragment) {
                    hide()
                } else {
                    show()
                }
            }
        }
    }

    private fun setUpNavigationWithDrawer() {
        NavigationUI.setupWithNavController(navigationView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.signUpFormFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
    }

    private fun observeSession() {
        sessionHolder.sessionLiveData
            .observe(this) {
                if (it == null) return@observe
                setUsername(it.username)
                onAuthentication(it.authenticated)
            }
    }

    private fun setUsername(username: String?) {
        headerUsernameTv.isVisible = username != null
        if (username != null) {
            headerUsernameTv.text = username
        }
    }

    private fun setNavHeaderClickListener() {
        navigationView.getHeaderView(0).setOnClickListener {
            navController.navigate(Uri.parse(getString(R.string.profile_deep_link)))
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun onAuthentication(isAuthenticated: Boolean?) {
        if (isAuthenticated !== null && isAuthenticated.not()) {
            navController.currentDestination?.id?.let { id ->
                if (id != R.id.loginFragment &&
                    id != R.id.signUpFormFragment
                ) {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true)
                        .build()
                    navController.navigate(
                        R.id.loginFragment,
                        null,
                        navOptions
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                return if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                } else {
                    false
                }
            }
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}