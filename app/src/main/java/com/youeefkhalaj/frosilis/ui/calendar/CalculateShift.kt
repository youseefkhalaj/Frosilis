package com.youeefkhalaj.frosilis.ui.calendar

import com.youeefkhalaj.frosilis.data.Shift

class CalculateShift(val shift: Shift) {


    fun calculateDaysYear(year: Int, month: Int, day: Int ): String {
        val y = year - 1401
        val y1 = if ( y >= 12){ (y * 365) + 3}
        else if (y >= 8 ){(y * 365) + 2}
        else if (y >= 3){(y * 365) + 1}
        else{y * 365}
        val m:Int
        val m1 = if ( month <= 6 ){
            m = month - 1
            m * 31}
        else{
            m = month - 7
            (m * 30) + 186
        }
        val sum = (y1 + m1 + day) % 8
        val returnShift:String
        if (shift == Shift.A){
            returnShift =  when (sum){
                1,2 -> "ا"
                3,4 -> "ر"
                5,6 -> "ع"
                else -> "ش"
            }
        }else if (shift == Shift.B){
            returnShift = when (sum){
                1,2 -> "ر"
                3,4 -> "ع"
                5,6 -> "ش"
                else -> "ا"
            }
        }else if (shift == Shift.C){
            returnShift =  when (sum){
                1,2 -> "ع"
                3,4 -> "ش"
                5,6 -> "ا"
                else -> "ر"
            }
        }else if (shift == Shift.D){
            returnShift = when (sum){
                1,2 -> "ش"
                3,4 -> "ا"
                5,6 -> "ر"
                else -> "ع"
            }
        }else{
            returnShift = when (sum){
                1,2 -> "ر"
                3,4 -> "ر"
                5,6 -> "ر"
                else -> "ا"
            }
        }
        return returnShift

    }
}