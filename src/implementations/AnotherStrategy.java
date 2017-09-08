package implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import forsale.*;

public class AnotherStrategy implements Strategy {
	@Override
	public int bid(PlayerRecord player, AuctionState auction) {
		int totalCashRemainingInAuction = 0;
		for (PlayerRecord p : auction.getPlayersInAuction()) {
			totalCashRemainingInAuction += p.getCash();
		}
		int totalPropertyValuesRemainingInAuction = 0;
		for (Card c : auction.getCardsInAuction()) {
			totalPropertyValuesRemainingInAuction += c.getQuality();
		}
		int totalPropertyValuesRemainingInDeck = 0;
		for (Card c : auction.getCardsInDeck()) {
			totalPropertyValuesRemainingInDeck += c.getQuality();
		}

		int worst = 6;
		int bad = 12;
		int average = 18;
		int good = 24;
		int best = 30;

		int auctionValue = 0;
		for (Card c : auction.getCardsInAuction()) {
			if (c.getQuality() <= 6) {
				auctionValue += 1;
			} else if (c.getQuality() <= 12) {
				auctionValue += 2;
			} else if (c.getQuality() <= 18) {
				auctionValue += 3;
			} else if (c.getQuality() <= 24) {
				auctionValue += 4;
			} else if (c.getQuality() <= 30) {
				auctionValue += 5;
			}
		}

		// if player.getCurrentBid = -1 player has dropped out
		// first array to store currentBid and next to store cashLeft
		int[] playersCashRemaining = new int[6];
		int playersRemaining = 0;
		int playersToBid = 0;
		int bidPosition = 0;

		int i = 0;
		for (PlayerRecord p : auction.getPlayersInAuction()) {
			//log(p.getName());
			playersCashRemaining[i] = p.getCash();
			if (p.getCurrentBid() != -1) playersRemaining++;
			if (p.getCurrentBid() == 0 ) playersToBid++;
			i++;
		}
		
		bidPosition = 6 - playersToBid;
		
		if (bidPosition == 1) {
			if (auctionValue <= worst) {
				auctionValue = 0;
				return -1;
			} else if (auctionValue <= bad) {
				auctionValue = 0;
				return auction.getCurrentBid() + 1;
			} else if (auctionValue <= average && player.getCash() > auction.getCurrentBid() && player.getCash() >= 3) {
				auctionValue = 0;
				return auction.getCurrentBid() + 3;
			} else if (auctionValue <= average && player.getCash() > auction.getCurrentBid()) {
				auctionValue = 0;
				return auction.getCurrentBid() + 1;
			} else if (auctionValue <= good && player.getCash() > auction.getCurrentBid() && player.getCash() >= 4) {
				auctionValue = 0;
				return auction.getCurrentBid() + 4;
			} else if (auctionValue <= good && player.getCash() > auction.getCurrentBid()) {
				auctionValue = 0;
				return auction.getCurrentBid() + 1;
			} else if (auctionValue <= best && player.getCash() > auction.getCurrentBid() && player.getCash() >= 5) {
				auctionValue = 0;
				return auction.getCurrentBid() + 5;
			} else if (auctionValue <= best && player.getCash() > auction.getCurrentBid()) {
				auctionValue = 0;
				return auction.getCurrentBid() + 1;
			} else {
				return -1;
			}
		}
		

		if (auctionValue <= worst) {
			auctionValue = 0;
			return -1;
		} else if (auctionValue <= bad) {
			auctionValue = 0;
			return -1;
		} else if (auctionValue <= average && player.getCash() > auction.getCurrentBid()) {
			auctionValue = 0;
			return auction.getCurrentBid() + 1;
		} else if (auctionValue <= good && player.getCash() > auction.getCurrentBid()) {
			auctionValue = 0;
			return auction.getCurrentBid() + 1;
		} else if (auctionValue <= best && player.getCash() > auction.getCurrentBid()) {
			auctionValue = 0;
			return auction.getCurrentBid() + 1;
		} else {
			return -1;
		}
	}

	@Override
	public Card chooseCard(PlayerRecord player, SaleState sale) {
		int totalChequesRemaining = 0;
		for (int cheque : sale.getChequesRemaining()) {
			totalChequesRemaining += cheque;
		}

		int totalPropertyValuesRemaining = 0;
		for (PlayerRecord p : sale.getPlayers()) {
			for (Card c : p.getCards()) {
				totalPropertyValuesRemaining += c.getQuality();
			}
		}

		double marketValue = totalChequesRemaining / totalPropertyValuesRemaining;

		ArrayList<Integer> chequesAvailable = new ArrayList<Integer>();
		chequesAvailable.addAll(sale.getChequesAvailable());
		Collections.sort(chequesAvailable);

		ArrayList<Card> heldCards = new ArrayList<Card>();
		heldCards.addAll(player.getCards());
		Collections.sort(heldCards, new CardComparator());

		ArrayList<Card> otherCards = new ArrayList<Card>();
		for (PlayerRecord p : sale.getPlayers()) {
			for (Card c : p.getCards()) {
				if (!heldCards.contains(c))
					otherCards.add(c);
			}
		}
		Collections.sort(otherCards, new CardComparator());

		// Find lowest card that exceeds the top value other card, and would be better
		// than market value
		for (int i = 0; i < otherCards.size(); i++) {
			Card otherCard = otherCards.get(otherCards.size() - i - 1);
			int chequeIndex = Math.max(0, chequesAvailable.size() - i - 1);
			for (Card c : heldCards) {
				if (c.getQuality() > otherCard.getQuality()
						&& c.getQuality() * marketValue >= chequesAvailable.get(chequeIndex)) {
					return c;
				}
			}
		}

		return heldCards.get(0);
	}

	private void log(String output) {
		System.out.println(output);
	}

	class CardComparator implements Comparator<Card> {
		@Override
		public int compare(Card c1, Card c2) {
			return c2.getQuality() - c1.getQuality();
		}
	}
}