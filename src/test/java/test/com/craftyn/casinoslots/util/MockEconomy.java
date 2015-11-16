package test.com.craftyn.casinoslots.util;

import java.util.List;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

public class MockEconomy extends AbstractEconomy {

    public EconomyResponse bankBalance(String name) {
        return null;
    }

    public EconomyResponse bankDeposit(String name, double arg1) {
        return null;
    }

    public EconomyResponse bankHas(String name, double arg1) {
        return null;
    }

    public EconomyResponse bankWithdraw(String name, double arg1) {
        return null;
    }

    public EconomyResponse createBank(String name, String arg1) {
        return null;
    }

    public boolean createPlayerAccount(String name) {
        return false;
    }

    public boolean createPlayerAccount(String arg0, String arg1) {
        return false;
    }

    public String currencyNamePlural() {
        return "Dollars";
    }
    
    public String currencyNameSingular() {
        return "Dollar";
    }

    public EconomyResponse deleteBank(String arg0) {
        return null;
    }

    public EconomyResponse depositPlayer(String arg0, double arg1) {
        return null;
    }

    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
        return null;
    }

    public String format(double arg0) {
        return null;
    }

    public int fractionalDigits() {
        return 0;
    }

    public double getBalance(String arg0) {
        return 0;
    }

    public double getBalance(String arg0, String arg1) {
        return 0;
    }

    public List<String> getBanks() {
        return null;
    }

    public String getName() {
        return "MockEconomy";
    }

    public boolean has(String arg0, double arg1) {
        return false;
    }

    public boolean has(String arg0, String arg1, double arg2) {
        return false;
    }

    public boolean hasAccount(String arg0) {
        return false;
    }

    public boolean hasAccount(String arg0, String arg1) {
        return false;
    }

    public boolean hasBankSupport() {
        return false;
    }

    public EconomyResponse isBankMember(String arg0, String arg1) {
        return null;
    }

    public EconomyResponse isBankOwner(String arg0, String arg1) {
        return null;
    }

    public boolean isEnabled() {
        return true;
    }

    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        return null;
    }

    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
        return null;
    }

}
