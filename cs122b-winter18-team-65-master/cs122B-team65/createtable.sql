CREATE SCHEMA IF NOT EXISTS `moviedb`;
USE `moviedb` ;
CREATE TABLE IF NOT EXISTS `moviedb`.`movies` (
  `id` VARCHAR(10) NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `year` INT NOT NULL,
  `director` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`));
  
CREATE TABLE IF NOT EXISTS `moviedb`.`stars` (
  `id` VARCHAR(10) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `birthYear` INT NULL,
  PRIMARY KEY (`id`));
  
  CREATE TABLE IF NOT EXISTS `moviedb`.`stars_in_movies` (
  `starId` VARCHAR(10) NOT NULL,
  `movieId` VARCHAR(10) NOT NULL,
  INDEX `starId_idx` (`starId` ASC),
  INDEX `movieId_idx` (`movieId` ASC),
  CONSTRAINT `starId`
    FOREIGN KEY (`starId`)
    REFERENCES `moviedb`.`stars` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `movieId`
    FOREIGN KEY (`movieId`)
    REFERENCES `moviedb`.`movies` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE TABLE IF NOT EXISTS `moviedb`.`genres` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`));
  
  CREATE TABLE IF NOT EXISTS `moviedb`.`genres_in_movies` (
  `genreId` INT NOT NULL,
  `movieId` VARCHAR(10) NOT NULL,
    FOREIGN KEY (`genreId`)
    REFERENCES `moviedb`.`genres` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (`movieId`)
    REFERENCES `moviedb`.`movies` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE TABLE IF NOT EXISTS `moviedb`.`creditcards` (
  `id` VARCHAR(20) NOT NULL,
  `firstName` VARCHAR(50) NOT NULL,
  `lastName` VARCHAR(50) NOT NULL,
  `expiration` DATE NOT NULL,
  PRIMARY KEY (`id`));
  
  
CREATE TABLE IF NOT EXISTS `moviedb`.`customers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(50) NOT NULL,
  `lastName` VARCHAR(50) NOT NULL,
  `ccId` VARCHAR(20) NOT NULL,
  `address` VARCHAR(200) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `ccId_idx` (`ccId` ASC),
  CONSTRAINT `ccId`
    FOREIGN KEY (`ccId`)
    REFERENCES `moviedb`.`creditcards` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE TABLE IF NOT EXISTS `moviedb`.`sales` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `customerId` INT NOT NULL,
  `movieId` VARCHAR(10) NOT NULL,
  `saleDate` DATE NOT NULL,
  PRIMARY KEY (`id`),
    FOREIGN KEY (`customerId`)
    REFERENCES `moviedb`.`customers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (`movieId`)
    REFERENCES `moviedb`.`movies` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE TABLE IF NOT EXISTS `moviedb`.`ratings` (
  `movieId` VARCHAR(10) NOT NULL,
  `rating` FLOAT NOT NULL,
  `numVotes` INT NOT NULL,
    FOREIGN KEY (`movieId`)
    REFERENCES `moviedb`.`movies` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);