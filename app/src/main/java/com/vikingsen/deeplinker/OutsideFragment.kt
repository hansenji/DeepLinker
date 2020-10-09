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
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
        binding.noticeButton.setOnClickListener {
            val deepLinkIntent = buildDeepLinkIntent("deeplinker://building/level1/GREEN/level2/RED")

            // https://developer.android.com/training/notify-user/build-notification#click
            val builder = NotificationCompat.Builder(requireContext(), getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Deep Link Available")
                .setContentText("Tap to go to Level 2")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(deepLinkIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(requireContext())) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }
    }

    private fun buildDeepLinkIntent(destination: String): PendingIntent {
        val deepLinkIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(destination)
            // Added seemed to make no difference in the backstack. If I understand correctly this may be because everything is running
            // in a single activity.
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), deepLinkIntent, PendingIntent.FLAG_ONE_SHOT)
    }
}