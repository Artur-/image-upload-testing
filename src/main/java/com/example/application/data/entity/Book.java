package com.example.application.data.entity;

import javax.persistence.Entity;

import com.example.application.data.AbstractEntity;
import java.time.LocalDate;

@Entity
public class Book extends AbstractEntity {

  private String image;
  private String name;
  private String author;
  private LocalDate publicationDate;
  private Double pages;
  private String isbn;
  private Double price;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public LocalDate getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(LocalDate publicationDate) {
    this.publicationDate = publicationDate;
  }

  public Double getPages() {
    return pages;
  }

  public void setPages(Double pages) {
    this.pages = pages;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

}
