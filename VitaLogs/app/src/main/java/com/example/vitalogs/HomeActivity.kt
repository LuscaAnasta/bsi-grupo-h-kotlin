package com.example.vitalogs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    // TODO: Criar e carregar o DashboardFragment
                    // selectedFragment = DashboardFragment()
                }
                R.id.nav_reports -> {
                    selectedFragment = ReportsFragment()
                }
                R.id.nav_goals -> {
                    // TODO: Criar e carregar o GoalsFragment
                    // selectedFragment = GoalsFragment()
                }
                R.id.nav_profile -> {
                    // TODO: Criar e carregar o ProfileFragment
                    // selectedFragment = ProfileFragment()
                }
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
            }
            true
        }

        // Define a tela de Relatórios como a tela inicial
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_reports
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ReportsFragment()).commit()
        }
    }
}