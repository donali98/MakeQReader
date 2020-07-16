package com.example.makeqreader.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.makeqreader.R
import com.example.makeqreader.adapters.ProductAdapter
import com.example.makeqreader.network.models.Product
import com.example.makeqreader.viewmodels.ProductListViewModel
import kotlinx.android.synthetic.main.fragment_product_list.*


/**
 * A simple [Fragment] subclass.
 * Use the [ProductListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductListFragment : Fragment() {

    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productAdapter = object :ProductAdapter(){
            override fun setClickListener(itemView: View, product: Product) {
                val nextAction = ProductListFragmentDirections.nextAction(product.id)
                Navigation.findNavController(itemView).navigate(nextAction)
            }
        }
        productListViewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        productListViewModel.productList.observe(this, Observer {products->

            if(products!= null && products.isNotEmpty())
                productAdapter.updateList(products)

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productListViewModel.retreiveProductsFromApi()
        rv_product_list.apply {
            setHasFixedSize(true)
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

}