/*
 * pBinTreeStringWR.java	v0.01
 *
 * Copyright(c) 1998, Particle
 */

import java.lang.*;
import java.io.*;

public class pBinTreeStringWR{
	public pBinTreeStringWR left,right;
	public String data;

	public static String[] strings = {"one","two",
		"three","four","five","six","seven","eight",
		"nine","ten","zero","computer","mouse","screen",
		"laptop","book","decimal","binary","quake"};

	public static pBinTreeStringWR tree_AddString(
		pBinTreeStringWR r,String s){
		if(r == null){
			r = new pBinTreeStringWR();
			r.left = r.right = null;
			r.data = s;
		}else if(r.data.compareTo(s) < 0)
			r.right = tree_AddString(r.right,s);
		else
			r.left = tree_AddString(r.left,s);
		return r;
	}

	public static void tree_InOrderPrint(
		pBinTreeStringWR r){
		if(r != null){
			tree_InOrderPrint(r.left);
			System.out.print(" "+r.data);
			tree_InOrderPrint(r.right);
		}
	}

	public static void tree_FileWrite(
		pBinTreeStringWR r,
		DataOutputStream output) throws IOException{
		if(r != null){
			byte[] tmp = r.data.getBytes();
			output.writeInt(tmp.length);
			output.write(tmp);
			tree_FileWrite(r.left,output);
			tree_FileWrite(r.right,output);
		}else
			output.writeInt(0);
	}

	public static pBinTreeStringWR tree_FileRead(
		pBinTreeStringWR r,
		DataInputStream input) throws IOException{
		int n = input.readInt();
		if(n != 0){
			byte[] tmp = new byte[n];
			input.read(tmp);
			r = new pBinTreeStringWR();
			r.data = new String(tmp);
			r.left = tree_FileRead(r.left,input);
			r.right = tree_FileRead(r.right,input);
		}else
			r = null;
		return r;
	}

	public static boolean tree_Compare(
		pBinTreeStringWR a,pBinTreeStringWR b){
		if(a != null && b != null){
			return a.data.compareTo(b.data) == 0 &&
				tree_Compare(a.left,b.left) &&
				tree_Compare(a.right,b.right);
		}else if(a == null && b == null)
			return true;
		else 	
			return false;
	}

	public static void main(String[] args){
		File file = new File("pBinTreeStringWR.dat");
		pBinTreeStringWR read_tree = null,tree = null;
		System.out.print("inserting: ");

		for(int i=0;i<strings.length;i++){
			String s = new String(strings[i]);
			System.out.print(" "+s);
			tree = tree_AddString(tree,s);
		}

		System.out.print("\ntree: ");
		tree_InOrderPrint(tree);
		
		System.out.println("\nwriting to "+file);
		try{
			tree_FileWrite(tree,
				new DataOutputStream(
				new FileOutputStream(file)));
		}catch(IOException e){
			System.out.println(e);
		}

		System.out.println("reading from "+file);
		try{
			read_tree = tree_FileRead(read_tree,
				new DataInputStream(
				new FileInputStream(file)));
		}catch(IOException e){
			System.out.println(e);
		}
		
		System.out.print("read tree: ");
		tree_InOrderPrint(read_tree);

		if(tree_Compare(tree,read_tree))
			System.out.println(
				"\nThe two trees are identical.");
		else
			System.out.println(
				"\nThe two trees are different.");
		System.out.println("done ;-)");
	}
}