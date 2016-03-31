package com.coen280.yelpWithUI;

import java.nio.file.Files;


import java.nio.file.Paths;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Populate extends DBConnect {

	Connection dbConnection = null;
	JSONParser parser = new JSONParser();

	public Populate() {
		try {
			dbConnection = getDBConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inserUsers() {
		// User
		List<String> userList = new ArrayList<>();
		PreparedStatement userStatement = null;
		int count = 0;
		try (Stream<String> stream = Files.lines(Paths.get("resources/yelp_user.json"))) {
			dbConnection.setAutoCommit(true);
			userList = stream.collect(Collectors.toList());
			for (String string : userList) {

				try {
					JSONObject jsonObject = (JSONObject) parser.parse(string);

					if (userStatement == null) {
						try {
							String sql = "INSERT INTO users(YELPING_SINCE, NAME, REVIEW_COUNT, USER_ID, AVERAGE_STARS, FRIENDS_COUNT, FANS)"
									+ " VALUES (?,?,?,?,?,?,?)";
							userStatement = dbConnection.prepareStatement(sql);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					System.out.println(userStatement);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
					java.util.Date parsed = format.parse(jsonObject.get("yelping_since").toString());
					userStatement.setDate(1, new Date(parsed.getTime()));
					userStatement.setString(2, jsonObject.get("name").toString());
					userStatement.setString(3, jsonObject.get("review_count").toString());
					userStatement.setString(4, jsonObject.get("user_id").toString());
					userStatement.setString(5, jsonObject.get("average_stars").toString());
					JSONArray friends = (JSONArray) jsonObject.get("friends");
					userStatement.setInt(6, friends.size());
					userStatement.setString(7, jsonObject.get("fans").toString());
					System.out.println(userStatement.toString());
					try {
						System.out.println(userStatement.executeUpdate());
					} catch (SQLException e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (userStatement != null) {
				try {
					userStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userStatement = null;
			}

		}
	}

	public void insertBusiness() {
		// Business

		PreparedStatement businessStatement = null;
		PreparedStatement hourStaement = null;
		PreparedStatement mainCategoryStatement = null;
		@SuppressWarnings("rawtypes")
		HashMap mainCategoriesHash = new HashMap();

		// Inserting main categories
		try {
			dbConnection.setAutoCommit(false);
			String categories = "Insert into CATEGORY (NAME) VALUES (?)";
			mainCategoryStatement = dbConnection.prepareStatement(categories);
			mainCategoryStatement.setString(1, "Active Life");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Arts & Entertainment");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Automotive");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Car Rental");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Cafes");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Beauty & Spas");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Convenience Stores");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Dentists");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Doctors");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Drugstores");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Department Stores");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Education");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Event Planning & Services");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Flowers & Gifts");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Food");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Health & Medical");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Home Services");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Home & Garden");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Hospitals");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Hotels & Travel");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Hardware Stores");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Grocery");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Medical Centers");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Nurseries & Gardening");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Nightlife");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Restaurants");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Shopping");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.setString(1, "Transportation");
			mainCategoryStatement.addBatch();
			mainCategoryStatement.executeBatch();
			dbConnection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			Statement cate = dbConnection.createStatement();
			ResultSet rs = cate.executeQuery("select * from category");
			ResultSetMetaData rsMeta = rs.getMetaData();
			while (rs.next()) {
				mainCategoriesHash.put(rs.getString(2), rs.getString(1));
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			List<String> list = new ArrayList<>();
			dbConnection.setAutoCommit(true);
			PreparedStatement sub_category_insert = null;
			PreparedStatement business_category_insert = null;
			PreparedStatement category_sub_cate_insert = null;
			PreparedStatement bus_cate_insert = null;
			try (Stream<String> stream = Files.lines(Paths.get("resources/yelp_business.json"))) {
				list = stream.collect(Collectors.toList());
				for (String string : list) {

					try {
						JSONObject jsonObject = (JSONObject) parser.parse(string);

						if (businessStatement == null) {
							try {
								String sql = "INSERT INTO business(BUSINESS_ID, FULL_ADDRESS, OPEN, CITY, REVIEW_COUNT, NAME, LONGITUDE, LATITUDE, NEIGHBORHOODS, STARS,STATE)"
										+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
								businessStatement = dbConnection.prepareStatement(sql);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (sub_category_insert == null) {
							try {
								String sql = "INSERT INTO sub_category(name)" + " VALUES (?)";
								sub_category_insert = dbConnection.prepareStatement(sql);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (category_sub_cate_insert == null) {
							try {
								String sql = "INSERT INTO CATEGORY_SUB_CATEGORY(CATEGORY_ID,SUB_CATEGORY_ID)"
										+ " VALUES (?,?)";
								category_sub_cate_insert = dbConnection.prepareStatement(sql);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (business_category_insert == null) {
							try {
								String sql = "INSERT INTO business_category(business_id,category_id)" + " VALUES (?,?)";
								business_category_insert = dbConnection.prepareStatement(sql);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (bus_cate_insert == null) {
							try {
								String sql = "INSERT INTO BUSINESS_CAT_SUB(business_id,category_name,SUB_CATEGORY_name)"
										+ " VALUES (?,?,?)";
								bus_cate_insert = dbConnection.prepareStatement(sql);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						System.out.println(businessStatement);
						int open = (jsonObject.get("open").toString().equals("true")) ? 1 : 0;
						businessStatement.setString(1, jsonObject.get("business_id").toString());
						businessStatement.setString(2, jsonObject.get("full_address").toString());
						businessStatement.setInt(3, open);
						businessStatement.setString(4, jsonObject.get("city").toString());
						businessStatement.setInt(5, Integer.parseInt(jsonObject.get("review_count").toString()));
						businessStatement.setString(6, jsonObject.get("name").toString());
						businessStatement.setString(7, jsonObject.get("latitude").toString());
						businessStatement.setString(8, jsonObject.get("longitude").toString());
						businessStatement.setString(9, jsonObject.get("neighborhoods").toString());
						businessStatement.setString(10, jsonObject.get("stars").toString());
						businessStatement.setString(11, jsonObject.get("state").toString());

						System.out.println(businessStatement.toString());
						JSONArray category = (JSONArray) jsonObject.get("categories");
						try {
							System.out.println(businessStatement.executeUpdate());
						} catch (SQLException e) {
							e.printStackTrace();
						}

						List<String> maincategory = new ArrayList<>();
						List<String> subCategory = new ArrayList<>();
						for (Object object : category) {
							if (mainCategoriesHash.get(object.toString()) != null) {
								maincategory.add(object.toString());
								// business_category_insert.setString(1,
								// jsonObject.get("business_id").toString());
								// business_category_insert.setInt(2,
								// Integer.parseInt(mainCategoriesHash.get(object.toString()).toString()));
								// business_category_insert.executeUpdate();
							} else {
								subCategory.add(object.toString());
								// sub_category_insert.setString(1,
								// object.toString());
								// try {
								// sub_category_insert.executeUpdate();
								// } catch (Exception e) {
								// System.out.println("Duplicate values");
								// }
								// String sql = "select sub_category_id from
								// sub_category where name = ?";
								// PreparedStatement cat_sub1 =
								// dbConnection.prepareStatement(sql);
								// cat_sub1.setString(1, object.toString());
								// ResultSet subCatRs = cat_sub1.executeQuery();
								// while (subCatRs.next()) {
								//
								// }
								// try {
								// cat_sub1.close();
								// subCatRs.close();
								// } catch (Exception e) {
								// e.printStackTrace();
								// }

							}
						}

						for (String cat : maincategory) {
							for (String sub_cat : subCategory) {
								bus_cate_insert.setString(1, jsonObject.get("business_id").toString());
								bus_cate_insert.setString(2, cat);
								bus_cate_insert.setString(3, sub_cat);
								// category_sub_cate_insert.setInt(1, cat);
								// category_sub_cate_insert.setInt(2, sub_cat);
								// try {
								// category_sub_cate_insert.executeUpdate();
								// } catch (SQLException e) {
								// System.out.println("Duplicate relation");
								// }
								try {
									bus_cate_insert.executeUpdate();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			finally {
				if (businessStatement != null) {
					businessStatement.close();
					businessStatement = null;
				}

				if (business_category_insert != null) {
					business_category_insert.close();
					business_category_insert = null;
				}
				if (sub_category_insert != null) {
					sub_category_insert.close();
					sub_category_insert = null;
				}
				if (category_sub_cate_insert != null) {
					category_sub_cate_insert.close();
					category_sub_cate_insert = null;
				}
				if (mainCategoryStatement != null) {
					mainCategoryStatement.close();
					mainCategoryStatement = null;
				}
				if (bus_cate_insert != null) {
					bus_cate_insert.close();
					bus_cate_insert = null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertChekin() {
		List<String> checkinList = new ArrayList<>();
		PreparedStatement checkinStatement = null;
		int count = 0;
		try (Stream<String> stream = Files.lines(Paths.get("resources/yelp_checkin.json"))) {
			dbConnection.setAutoCommit(true);
			checkinList = stream.collect(Collectors.toList());
			System.out.println(checkinList.size());
			for (String string : checkinList) {

				try {
					JSONObject jsonObject = (JSONObject) parser.parse(string);

					if (checkinStatement == null) {
						try {
							String sql = "INSERT INTO CHECK_IN (dayandtime, IN_COUNT, BUSINESS_ID)"
									+ " VALUES (?,?,?)";
							checkinStatement = dbConnection.prepareStatement(sql);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					// System.out.println(checkinStatement);
					JSONObject checkinTime = (JSONObject) jsonObject.get("checkin_info");
					System.out.println(checkinTime);
					Set<String> keys = checkinTime.keySet();
					for (String temp : keys) {
						System.out.println(temp);
						System.out.println(checkinTime.get(temp));
						checkinStatement.setString(2, checkinTime.get(temp).toString());
						String[] hourday = temp.split("-");
						Float fHrsDay = Integer.parseInt(hourday[1]) + Integer.parseInt(hourday[0]) / 24F;
						checkinStatement.setFloat(1, fHrsDay);
						checkinStatement.setString(3, jsonObject.get("business_id").toString());
						checkinStatement.addBatch();
						count++;
						if(count > 200)
						try {
							System.out.println(checkinStatement.executeBatch());
							count++;
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (checkinStatement != null) {
				try {
					checkinStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				checkinStatement = null;
			}

		}
	}

	public void insertReview() {
		List<String> reviewList = new ArrayList<>();
		PreparedStatement reviewStatement = null;
		int count = 0;
		try (Stream<String> stream = Files.lines(Paths.get("resources/yelp_review.json"))) {
			dbConnection.setAutoCommit(true);
			reviewList = stream.collect(Collectors.toList());
			System.out.println(reviewList.size());
			for (String string : reviewList) {

				try {
					JSONObject jsonObject = (JSONObject) parser.parse(string);

					if (reviewStatement == null) {
						try {
							String sql = "INSERT INTO REVIEW(REVIEW_ID, DATE_OF_REVIEW, STARS, USER_ID, BUSINESS_ID, TEXT,VOTES) "
									+ " VALUES (?,?,?,?,?,?,?)";
							reviewStatement = dbConnection.prepareStatement(sql);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date parsed = format.parse(jsonObject.get("date").toString());
					reviewStatement.setDate(2, new Date(parsed.getTime()));
					reviewStatement.setString(1, jsonObject.get("review_id").toString());
					reviewStatement.setString(3, jsonObject.get("stars").toString());
					reviewStatement.setString(4, jsonObject.get("user_id").toString());
					reviewStatement.setString(5, jsonObject.get("business_id").toString());
					reviewStatement.setString(6, jsonObject.get("text").toString());
					JSONObject votes = (JSONObject) jsonObject.get("votes");

					int votesCount = 0;
					// System.out.println(votes);
					Set<String> keys = votes.keySet();
					for (String temp : keys) {
						votesCount = votesCount + Integer.parseInt((votes.get(temp).toString()));
					}
					reviewStatement.setInt(7, votesCount);
					reviewStatement.addBatch();
					count++;
					try {
						System.out.println(reviewStatement.executeBatch());
						count = 0;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reviewStatement != null) {
				try {
					reviewStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				reviewStatement = null;
			}

		}
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		Populate ob =  new Populate();
		//ob.insertBusiness();
		//ob.inserUsers();
		ob.insertChekin();
		ob.insertReview();

	}
}
