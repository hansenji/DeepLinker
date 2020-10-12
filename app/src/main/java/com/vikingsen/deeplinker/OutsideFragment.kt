package com.vikingsen.deeplinker

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vikingsen.deeplinker.databinding.OutsideFragmentBinding

class OutsideFragment : Fragment() {

    private lateinit var binding: OutsideFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = OutsideFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lobbyButton.setOnClickListener {
            findNavController().navigate(OutsideFragmentDirections.actionToLobbyFragment())
        }
        binding.level1Button.setOnClickListener {
            findNavController().navigate(Level1Fragment.getUri("BLUE"))
        }
        binding.level2Button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Level2Fragment.getOutsideUri("RED", "YELLOW")).apply {
                // This treats the Deep link like an external DeepLink vs DeepLinkRequest
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
        binding.noticeButton.setOnClickListener {
            val deepLinkIntent = buildDeepLinkIntent(Level2Fragment.getOutsideUri("PINK", "ORANGE"))

            // Don't use this because you have to add args for every start destination you go through Uri's are easier. See Line 46
//            val deepLinkIntent = NavDeepLinkBuilder(requireContext())
//                .setGraph(R.navigation.main_nav)
//                .setDestination(R.id.level2Fragment)
//                .setArguments(Level2FragmentArgs("ORANGE").toBundle().apply {
//                    putString("code1", "PINK")
//                })
//                .createPendingIntent()

            // https://developer.android.com/training/notify-user/build-notification#click
            val builder = NotificationCompat.Builder(requireContext(), getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Deep Link Available")
                .setContentText("Tap to go to Level 2")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                // This will launch a new task.
                // Creates a full backstack
                .setContentIntent(deepLinkIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(requireContext())) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }
    }

    private fun buildDeepLinkIntent(destinationUri: Uri): PendingIntent {
        val deepLinkIntent = Intent(Intent.ACTION_VIEW, destinationUri)

        return PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), deepLinkIntent, PendingIntent.FLAG_ONE_SHOT)
    }
}