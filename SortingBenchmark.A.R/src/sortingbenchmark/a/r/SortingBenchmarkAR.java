package sortingbenchmark.a.r;

import java.util.Random;
import java.util.Stack;


public class SortingBenchmarkAR {

    public static void main(String[] args) {
        String[] algorithms = {"Insertion", "Merge", "Heap", "Quick", "RandomizedQuick"};
        String[] dataTypes = {"Random", "FewUnique", "Sorted", "Reverse"};
        int[] sizes = {1000, 10000, 50000, 100000};

        System.out.println("*** SORTING BENCHMARK ***");

        for (String type : dataTypes) {
            System.out.println("\n~~~ DATA TYPE: " + type + "~~~");

            for (int size : sizes) {
                System.out.println("\nSize = " + size);

                int[] arr = generateData(type, size);

                for (String algo : algorithms) {

                    int[] Datacopy = arr.clone();

                    long start = System.currentTimeMillis();

                   
                    if (algo.equals("Insertion")) {
                        insertionSort(Datacopy);
                    } else if (algo.equals("Merge")) {
                        mergeSort(Datacopy, 0, Datacopy.length - 1);
                    } else if (algo.equals("Heap")) {
                        heapSort(Datacopy);
                    } else if (algo.equals("RandomizedQuick ")) {
                        randomizedQuickSort(Datacopy, 0, Datacopy.length - 1);
                    }
                    if ((algo.equals("Quick"))
                            && (type.equals("FewUnique") || type.equals("Sorted") || type.equals("Reverse"))) {

                        System.out.println("Quick Sort: skipped (worst-case input) causes StackOverFlow error");
                        continue;
                    } else if (algo.equals("Quick")) {
                        quickSort(Datacopy, 0, Datacopy.length - 1);
                    }

                    long end = System.currentTimeMillis();

                    System.out.println(algo + " Sort: " + (end - start) + " ms");
                }
            }
        }

        System.out.println("\n *** finish *** ");
    }

    //  DATA GENERATION 
    public static int[] generateData(String type, int size) {
        int[] arr = new int[size];
        Random r = new Random();

       switch (type) {
            case "Random":
                for (int i = 0; i < size; i++) arr[i] = r.nextInt(1_000_000);
                break;

            case "Sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                break;

            case "Reverse":
                for (int i = 0; i < size; i++) arr[i] = size - i;
                break;

            case "FewUnique":
                int[] vals = {1, 2, 3, 4, 5};
                for (int i = 0; i < size; i++) arr[i] = vals[r.nextInt(5)];
                break;
        }
        return arr;
    }
    
    //ALGORITHMS

    //  INSERTION SORT 
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
    
   

    //  MERGE SORT 
    public static void mergeSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = (left + right) / 2;

        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);

        merge(arr, left, mid, right);
    }

    public static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1) {
            arr[k++] = L[i++];
        }
        while (j < n2) {
            arr[k++] = R[j++];
        }
    }

    //  HEAP SORT 
    public static void heapSort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i >= 0; i--) {
               swap(arr, 0, i);

            heapify(arr, i, 0);
        }
    }

    public static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int L = 2 * i + 1;
        int R = 2 * i + 2;

        if (L < n && arr[L] > arr[largest]) {
            largest = L;
        }
        if (R < n && arr[R] > arr[largest]) {
            largest = R;
        }

        if (largest != i) {
           swap(arr, i, largest);

            heapify(arr, n, largest);
        }
    }

// QUICK SORT 

    public static void quickSort(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }

        int p = partition(arr, low, high);
        quickSort(arr, low, p - 1);
        quickSort(arr, p + 1, high);
    }

    //   Randomized Quick Sort 
    public static void  randomizedQuickSort(int[] arr, int low, int high) {
        Stack<Integer> stack = new Stack<>();
        stack.push(low);
        stack.push(high);

        while (!stack.isEmpty()) {
            high = stack.pop();
            low = stack.pop();

            if (low < high) {
           
                randomPivot(arr, low, high);

                
                int pi = partition(arr, low, high);

                
                stack.push(low);
                stack.push(pi - 1);
                stack.push(pi + 1);
                stack.push(high);
            }
        }
    }

    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

   
    public static void randomPivot(int[] arr, int low, int high) {
        Random rand = new Random();
        int pivotIndex = low + rand.nextInt(high - low + 1);

     
        int temp = arr[pivotIndex];
        arr[pivotIndex] = arr[high];
        arr[high] = temp;

    }}
