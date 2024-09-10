package com.example.connecturpet

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.connecturpet.databinding.ActivityMainBinding
import com.example.connecturpet.viewModel.SharedViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        sharedViewModel.userId.value = id

        val userType = intent.getStringExtra("userType")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()


        //MENU GIVER
        if (userType == "ROLE_GIVER") {
            //cambiando en el xml
            navController.graph = navController.navInflater.inflate(R.navigation.main_giver_graph)

            binding.bottomNavigation.setupWithNavController(navController)
            binding.bottomNavigation.menu.clear()
            binding.bottomNavigation.inflateMenu(R.menu.giver_menu)

            // Agregar listener al NavController para detectar cambios en los destinos del menu
            navController.addOnDestinationChangedListener { _, destination, _ ->
                // Ocultar bottomNavigationView si el destino actual no requiere mostrarlo
                if (destination.id == R.id.giverHomeFrag || destination.id == R.id.notificationFrag || destination.id == R.id.givEditProfFrag) {
                    binding.bottomNavigation.visibility = View.VISIBLE
                } else {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }

        } else{ //MENU ADOPTER, lo mismo de arriba para adopter

            navController.graph = navController.navInflater.inflate(R.navigation.main_adopter_graph)

            binding.bottomNavigation.setupWithNavController(navController)
            binding.bottomNavigation.menu.clear()
            binding.bottomNavigation.inflateMenu(R.menu.adopter_menu)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.adopterHomeFrag || destination.id == R.id.adoptEditProfFrag) {
                    binding.bottomNavigation.visibility = View.VISIBLE
                } else {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}
