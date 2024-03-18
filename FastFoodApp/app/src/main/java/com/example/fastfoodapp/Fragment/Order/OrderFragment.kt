package com.example.fastfoodapp.Fragment.Order

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastfoodapp.Adapter.OrderListAdapter
import com.example.fastfoodapp.Model.Order
import com.example.fastfoodapp.databinding.FragmentOrderBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding

    private lateinit var databaseReference: DatabaseReference

    private var order : Order ? =null
    private var orderList = mutableListOf<Order>()
    private lateinit var orderListAdapter: OrderListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference

        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            val userId = user.uid

            orderListAdapter = OrderListAdapter(requireContext(), orderList)
            binding.rvOrderList.adapter = orderListAdapter
            binding.rvOrderList.layoutManager = LinearLayoutManager(requireContext())

            databaseReference.child("orders").child(userId).child("orders")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        orderList.clear()

                        for (orderSnapshot in snapshot.children) {
                            val order = orderSnapshot.getValue(Order::class.java)
                            order?.let {
                                orderList.add(it)
                            }
                        }

                        orderListAdapter.setOrderList(orderList)
                        binding.tvPrice.text = "$ ${orderListAdapter.getTotalPrice()}"

                        orderListAdapter.onItemDeleteClickListener = { position, order ->
                            try {
                                showBottomSheetDailog(position, order)
                                orderListAdapter.notifyDataSetChanged()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle onCancelled
                    }
                })
        }
    }


    private fun showBottomSheetDailog(position: Int, order: Order) {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete")
            .setMessage("Are you sure you want to remove this item from your orderlist?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                deleteItem(position, order)
            }).setNegativeButton("Cancel",DialogInterface.OnClickListener { dialog, which ->

            })
        var dialog =builder.create()
            dialog.show()
    }

    private fun deleteItem(position: Int, order: Order) {
        val orderId = order.orderId // Assuming you have an 'orderId' field in your Order model

        // Remove the order from Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val orderRef = databaseReference.child("orders").child(userId).child("orders").child(orderId!!)
            orderRef.removeValue()
                .addOnSuccessListener {
                    // If deletion from Firebase is successful, update the local order list
                    orderList.removeAt(position)
                    orderListAdapter.notifyItemRemoved(position)
                    orderListAdapter.notifyItemRangeChanged(position, orderList.size)

                    // Update total price display
                    binding.tvPrice.text = "$ ${orderListAdapter.getTotalPrice()}"

                    // Show a message to indicate successful deletion
                    Toast.makeText(requireContext(), "Item removed from order list", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Handle failure scenario
                    // You can show an error message to the user
                    Toast.makeText(requireContext(), "Failed to remove item from order list", Toast.LENGTH_SHORT).show()
                }
        }
    }
}