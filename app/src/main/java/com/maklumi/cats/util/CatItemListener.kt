package com.maklumi.cats.util

import com.maklumi.cats.model.sqlpart.Cat

class CatItemListener(val pendengar: (catId: Int) -> Unit) {
    fun bilaKelik(cat: Cat) = pendengar(cat.uuid)
}