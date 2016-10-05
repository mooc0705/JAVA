package observe;

public class Main {

    public static void main(String[] args) {
        Product banana = new Product("banana", 0.59, "Loblaw");
        Product cereal = new Product("cereal", 7.49, "Target");

        Shopper anya = new Shopper("Jen Campbell");
        Shopper paco = new Shopper("Steve Engels");

        PriceWatchWebsite priceWatchRUs = new PriceWatchWebsite("www.pricewatchrus.com");

        banana.addObserver(paco);
        cereal.addObserver(paco);
        cereal.addObserver(anya);
        cereal.addObserver(priceWatchRUs);
        priceWatchRUs.addObserver(anya);

        banana.changePrice(0.55);
        cereal.changePrice(10 / 3.0);
        cereal.changePrice(15.42);    

        /* Output:
            Steve Engels was notified about a price change of banana at Loblaw to 0.55.
            You are subscribed to www.pricewatchrus.com.
            It was notified about a price change of cereal at Target to 3.33.
            Jen Campbell was notified about a price change of cereal at Target to 3.33.
            Jen Campbell was notified about a price change of cereal at Target to 3.33.
            Steve Engels was notified about a price change of cereal at Target to 3.33.
            You are subscribed to www.pricewatchrus.com.
            It was notified about a price change of cereal at Target to 15.42.
            Jen Campbell was notified about a price change of cereal at Target to 15.42.
            Jen Campbell was notified about a price change of cereal at Target to 15.42.
            Steve Engels was notified about a price change of cereal at Target to 15.42.
         */
        /* Output for new version (for next course offering):
        Jen Campbell was notified about a price change of cereal at Target to 3.33 on Tue Nov 04 23:33:08 EST 2014.
        Jen Campbell was notified about a price change of cereal at Target to 3.33 on Tue Nov 04 23:33:08 EST 2014.
        Steve Engels was notified about a price change of cereal at Target to 3.33 on Tue Nov 04 23:33:08 EST 2014.
        You are subscribed to www.pricewatchrus.com.
        It was notified about a price change of cereal at Target to 15.42 on Tue Nov 04 23:33:08 EST 2014.
        Jen Campbell was notified about a price change of cereal at Target to 15.42 on Tue Nov 04 23:33:08 EST 2014.
        Jen Campbell was notified about a price change of cereal at Target to 15.42 on Tue Nov 04 23:33:08 EST 2014.
        Steve Engels was notified about a price change of cereal at Target to 15.42 on Tue Nov 04 23:33:08 EST 2014.
        */
     }
}
