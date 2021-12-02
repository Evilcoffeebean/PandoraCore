package dev.pandora.core.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Waffle on 13/03/2017.
 */
public class Maths
{
    public static String toRomanNumeral(int number)
    {
        TreeMap<Integer, String> map = new TreeMap();
        map.put(Integer.valueOf(1000), "M");
        map.put(Integer.valueOf(900), "CM");
        map.put(Integer.valueOf(500), "D");
        map.put(Integer.valueOf(400), "CD");
        map.put(Integer.valueOf(100), "C");
        map.put(Integer.valueOf(90), "XC");
        map.put(Integer.valueOf(50), "L");
        map.put(Integer.valueOf(40), "XL");
        map.put(Integer.valueOf(10), "X");
        map.put(Integer.valueOf(9), "IX");
        map.put(Integer.valueOf(5), "V");
        map.put(Integer.valueOf(4), "IV");
        map.put(Integer.valueOf(1), "I");

        int l = ((Integer)map.floorKey(Integer.valueOf(number))).intValue();
        if (number == l) {
            return (String)map.get(Integer.valueOf(number));
        }
        return (String)map.get(Integer.valueOf(l)) + toRomanNumeral(number - l);
    }

    public static int round(int num, int multiple)
    {
        return multiple * Math.round(num / multiple);
    }

    public static Long getHowMuchLonger(Long startingTime, Long waitTime)
    {
        return Long.valueOf(waitTime.longValue() - (System.currentTimeMillis() - startingTime.longValue()));
    }

    public static String secondsToString(long millis)
    {
        long second = millis / 1000L % 60L;
        long minute = millis / 60000L % 60L;
        long hour = millis / 3600000L % 24L;
        long day = millis / 86400000L;

        String time = day + "d " + hour + "h " + minute + "m " + second + "s";
        String[] arrayOfString;
        int j = (arrayOfString = time.split(" ")).length;
        for (int i = 0; i < j; i++)
        {
            String lt = arrayOfString[i];
            if (lt.contains("d"))
            {
                if (lt.equals("0d")) {
                    time = time.replace("0d ", "");
                }
            }
            else if (lt.contains("h"))
            {
                if (lt.equals("0h")) {
                    time = time.replace("0h ", "");
                }
            }
            else if (lt.contains("m"))
            {
                if (lt.equals("0m")) {
                    time = time.replace("0m ", "");
                }
            }
            else if ((lt.contains("s")) &&
                    (lt.equals("0s")))
            {
                time = time.replace(" 0s", "");
                time = time.replace("0s", "");
            }
        }
        return time;
    }

    public static String secondsToString(int milliseconds)
    {
        return secondsToString(Long.parseLong(String.valueOf(milliseconds)));
    }

    public static double percent(double x, double z)
    {
        NumberFormat formatter = new DecimalFormat("#0.0");
        double perc = x / z * 100.0D;
        return Double.parseDouble(formatter.format(perc));
    }

    public static double distance(int x1, int z1, int x2, int z2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2.0D) + Math.pow(z2 - z1, 2.0D));
    }

    public static List<Block> getBlocksInRadius(Location loc, int radius, boolean yCheck)
    {
        Location l = loc.clone();
        World w = l.getWorld();
        int xCoord = (int)l.getX();
        int zCoord = (int)l.getZ();
        int YCoord = (int)l.getY();
        List<Block> tempList = new ArrayList();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (yCheck) {
                    for (int y = -radius; y <= radius; y++) {
                        tempList.add(new Location(w, xCoord + x, YCoord + y, zCoord + z).getBlock());
                    }
                } else {
                    tempList.add(new Location(w, xCoord + x, YCoord, zCoord + z).getBlock());
                }
            }
        }
        return tempList;
    }

    public static List<Block> getBlocksInRadius(Location loc, int radius, boolean yCheck, Material material)
    {
        Location l = loc.clone();
        World w = l.getWorld();
        int xCoord = (int)l.getX();
        int zCoord = (int)l.getZ();
        int YCoord = (int)l.getY();
        List<Block> tempList = new ArrayList();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (yCheck)
                {
                    for (int y = -radius; y <= radius; y++)
                    {
                        Block block = new Location(w, xCoord + x, YCoord + y, zCoord + z).getBlock();
                        if (block.getType() == material) {
                            tempList.add(block);
                        }
                    }
                }
                else
                {
                    Block block = new Location(w, xCoord + x, YCoord, zCoord + z).getBlock();
                    if (block.getType() == material) {
                        tempList.add(block);
                    }
                }
            }
        }
        return tempList;

    }
}