package com.bookmygift.config;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class MainClass {

	public static void main(String[] args) {
		
		BinaryOperator<Integer> binaryOperator = (x, y) -> x + y;
	        System.out.println(binaryOperator.apply(1, 2));
	        
	        // New way of declaring type of lambda parameters in Java 11
	        BiFunction<Integer, Integer, Integer> newWay = (var a, var b) -> a + b;
	        System.out.println(newWay.apply(1, 2));
		
	}

}
